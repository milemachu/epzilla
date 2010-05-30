package org.epzilla.dispatcher.logs;

import org.epzilla.dispatcher.xml.LogFileSettingReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
* initializing method which will accept List<String> and trigger ID, trigger owner
* default log file location is taken from the CurrentValues class
* size of the log file is defined, here it is 2MB, if greater than 1MB it will destroy existing an create new one
* owerwriteLog and write methods are there to perform that task
*/
public class WriteLog {
    private static LogFileSettingReader reader = new LogFileSettingReader();
    private static String filePath = "";
    private static boolean isLoaded = false;

    public WriteLog() {
    }

    public static void writeInit(List<String> triggerList, String userID) throws IOException {
        if (isLoaded == false) {
            loadSettings();
        }
        long size = getFileSize(filePath);

        if (size > 100 * 1024 * 1024)
            overwriteLog(CurrentValues.defaultLogFile, triggerList, userID);
        else
            writeLog(CurrentValues.defaultLogFile, triggerList, userID);
    }

    private static void overwriteLog(String filename, List<String> myArr, String ownerId) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false));
        String tag = ownerId;
        writer.write(tag + " " + "Checkpoint");
        writer.newLine();
        for (int i = 0; i < myArr.size(); i++) {
            writer.write(myArr.get(i));
            writer.newLine();
        }
        writer.write("</commit>");

        writer.newLine();
        writer.flush();
        writer.close();
    }

    private static void writeLog(String filename, List<String> myArr, String ownerId) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true));
        String tag = ownerId;
        writer.write(tag + " " + "Checkpoint");
        writer.newLine();
        for (int i = 0; i < myArr.size(); i++) {
            writer.write(myArr.get(i)+":"+ownerId);
            writer.newLine();
        }
        writer.write("</commit>");
        writer.newLine();
        writer.flush();
        writer.close();
    }

    private static void loadSettings() {
        try {
            ArrayList<String[]> data = reader.getServerIPSettings("./src/settings/log_file_settings.xml");
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
