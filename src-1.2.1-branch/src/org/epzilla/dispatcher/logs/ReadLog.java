package org.epzilla.dispatcher.logs;

import org.epzilla.dispatcher.xml.LogFileSettingReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadLog {
    private static LogFileSettingReader reader = new LogFileSettingReader();
    private static File file;
    private static boolean isLoaded = false;

    public static List<String> readLog(String clusterID) {
        if (!isLoaded) {
            loadSettings();
        }
        return FileScanner.readFile(file, clusterID);
    }

    public static ArrayList<String> readLog() throws FileNotFoundException {
        if (!isLoaded) {
            loadSettings();
        }
        ArrayList<String> recList;
        recList= FileScanner.readFile(file);
        return recList;
    }

    private static void loadSettings() {
        try {
            ArrayList<String[]> data = reader.getServerIPSettings("log_file_settings.xml");
            String[] ar = data.get(0);
            String filePath = ar[0];
            file = new File(filePath);
            isLoaded = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

