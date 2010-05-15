package org.epzilla.dispatcher.logs;

import org.epzilla.dispatcher.xml.LogFileSettingReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
* FileScanner class takes the Cluster ID and recover them
* ID is in the form:- CID0, CID1.....
* RecoveryList class takes log file and create undo and redo lists
*/
public class ReadLog {
    private static LogFileSettingReader reader = new LogFileSettingReader();
    private static String filePath = "";
    private static File file;
    private static String reqstr = "CID2";
    private static boolean isLoaded = false;
    private static List<String> arr = null;

    public static List<String> readLog(String clusterID) {
        if (isLoaded == false) {
            loadSettings();
        }
        arr = FileScanner.readFile(file, clusterID);
        return arr;
    }

    public static boolean readLog() {
        if (isLoaded == false) {
            loadSettings();
        }
        FileScanner.readFile(file);
        return true;
    }

    private static void loadSettings() {
        try {
            ArrayList<String[]> data = reader.getServerIPSettings("./src/settings/log_file_settings.xml");
            String[] ar = data.get(0);
            filePath = ar[0];
            file = new File(filePath);
            isLoaded = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        readLog();
    }
}

