package org.epzilla.dispatcher.logs;

import org.epzilla.dispatcher.xml.LogFileSettingReader;
import org.epzilla.util.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by IntelliJ IDEA.
 * Initialize the read log file method in the File Scanner class 
 * Author: Chathura
 * Date: Mar 19, 2010
 * Time: 6:21:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReadLog {
    private static LogFileSettingReader reader = new LogFileSettingReader();
    private static File file;
    private static boolean isLoaded = false;

    /**
     * Replay logs as requested by the client
     * @param clusterID
     * @return
     */
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
        recList = FileScanner.readFile(file);
        return recList;
    }

    /**
     *  load setting details of the log file
     */
    private static void loadSettings() {
        try {
            ArrayList<String[]> data = reader.getServerIPSettings("log_file_settings.xml");
            String[] ar = data.get(0);
            String filePath = ar[0];
            file = new File(filePath);
            isLoaded = true;
        } catch (IOException e) {
            Logger.error("I/O exception: ",e);

        }
    }
}

