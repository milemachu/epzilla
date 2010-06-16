package org.epzilla.dispatcher.logs;

import org.epzilla.dispatcher.xml.LogFileSettingReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
* initializing method which will accept List<String> and clientID,  clusterID
*  size of the log file is defined, here it is 2MB, if greater than 1MB it will destroy existing an create new one
* owerwriteLog and write methods are there to perform that task
*/
public class WriteLog {
    private static LogFileSettingReader reader = new LogFileSettingReader();
    private static String filePath = "";
    private static boolean isLoaded = false;

    public WriteLog() {
    }

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

    public static long getFileSize(String filename) {
        File file = new File(filename);
        if (!file.exists() || !file.isFile()) {

            return -1;
        }
        return file.length();
    }
}
