/***************************************************************************************************
Copyright 2009 - 2010 Harshana Eranga Martin, Dishan Metihakwala, Rajeev Sampath, Chathura Randika

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
****************************************************************************************************/
package org.epzilla.dispatcher.logs;

import org.epzilla.dispatcher.xml.LogFileSettingReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * initializing method which will accept List<String> and clientID,  clusterID
 * size of the log file is defined, here it is 2MB, if greater than 1MB it will destroy existing an create new one
 * owerwriteLog and write methods are there to perform that task
 * Author: Chathura
 * Date: Mar 19, 2010
 * Time: 7:10:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class WriteLog {
    private static LogFileSettingReader reader = new LogFileSettingReader();
    private static String filePath = "";
    private static boolean isLoaded = false;

    public WriteLog() {
    }

    /**
     * This method start writing to log file
     * @param triggerList
     * @param clientID
     * @param clusterID
     * @throws IOException
     */
    public static void writeInit(List<String> triggerList, String clientID, String clusterID) throws IOException {
        if (!isLoaded) {
            loadSettings();
        }
        long size = getFileSize(filePath);

        if (size > 1024 * 1024 * 1024)
            overwriteLog(filePath, triggerList, clientID, clusterID);
        else
            writeLog(filePath, triggerList, clientID, clusterID);
    }

    /**
     * check whether overwriting/clear and re write to log is needed
     * @param filename
     * @param myArr
     * @param clientID
     * @param clusterID
     * @throws IOException
     */
    private static void overwriteLog(String filename, List<String> myArr, String clientID, String clusterID) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false));
        String tag = "CID" + clusterID;
        writer.write(tag + " " + "Checkpoint");
        writer.newLine();
        for (String trigger : myArr) {
            writer.write(trigger + ":" + clientID + ":" + clusterID);
            writer.newLine();
        }
        writer.write("</commit>");

        writer.newLine();
        writer.flush();
        writer.close();
    }

    /**
     * This method log triggers to the checkpoint file
     * @param filename
     * @param myArr
     * @param clientID
     * @param clusterID
     * @throws IOException
     */
    private static void writeLog(String filename, List<String> myArr, String clientID, String clusterID) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true));
        String tag = "CID" + clusterID;
        writer.write(tag + " " + "Checkpoint");
        writer.newLine();
        for (String trigger : myArr) {
            writer.write(trigger + ":" + clientID + ":" + clusterID);
            writer.newLine();
        }
        writer.write("</commit>");
        writer.newLine();
        writer.flush();
        writer.close();
    }

    /**
     * Load settings data
     */
    private static void loadSettings() {
        try {
            ArrayList<String[]> data = reader.getServerIPSettings("log_file_settings.xml");
            String[] ar = data.get(0);
            filePath = ar[0];
            isLoaded = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
   method get the current file size
    */

    public static long getFileSize(String filename) {
        File file = new File(filename);
        if (!file.exists() || !file.isFile()) {

            return -1;
        }
        return file.length();
    }
}
