package org.epzilla.dispatcher.logs;

import org.epzilla.util.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * This class is used to read the Log file which contain the checkpoint details of the Triggers
 * Author: Chathura
 * Date: Mar 1, 2010
 * Time: 10:21:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class FileScanner implements Runnable {
    public static ArrayList<String> triggerList = new ArrayList<String>();
    private List<String> undoList = new ArrayList<String>();
    private File file;
    private static String strmatch = "";
    private static Scanner scanner = null;
    private static Matcher m1 = null;
    private static Matcher m2 = null;
    private static String st1 = "";
    private static String st2 = "";
    private static Pattern p1 = Pattern.compile("^[CID0-9]+ (.{10})$");
    private static Pattern p2 = Pattern.compile("</commit>");

    public FileScanner(File dd, List<String> recArray) {
        this.file = dd;
        this.undoList = recArray;
    }

    public FileScanner() {

    }

    public void run() {
        for (String anUndoList : undoList) {
            readFile(file, anUndoList);

        }
    }

    public static List<String> readFile(File file, String strReq) {
        List<String> recoverArr = new ArrayList<String>();
        long start = System.currentTimeMillis();
        try {
            scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                st1 = scanner.nextLine();
                m1 = p1.matcher(st1);
                m2 = p2.matcher(st1);
                if (m1.find()) {
                    StringTokenizer st = new StringTokenizer(st1);
                    strmatch = st.nextToken();
                    if (strmatch.equals(strReq)) {
                        while (scanner.hasNextLine()) {
                            st2 = scanner.nextLine();
                            m2 = p2.matcher(st2);
                            m1 = p1.matcher(st2);
                            if (m2.find()) {
                                break;
                            } else if (m1.find()) {
                                break;
                            } else
                                recoverArr.add(st2);
                        }
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            Logger.log("File not found");
        }
        printArray(recoverArr);
        long end = System.currentTimeMillis();
        Logger.log("Time: " + (end - start));
        return recoverArr;
    }

    /**
     * This method reads the log file and return the trigger list as an array list
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static ArrayList<String> readFile(File file) throws FileNotFoundException {
        ArrayList<String> recoverArr = new ArrayList<String>();
        long start = System.currentTimeMillis();

        scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            st1 = scanner.nextLine();
            m1 = p1.matcher(st1);
            m2 = p2.matcher(st1);
            if (m1.find()) {
                StringTokenizer st = new StringTokenizer(st1);
                strmatch = st.nextToken();
                while (scanner.hasNextLine()) {
                    st2 = scanner.nextLine();
                    m2 = p2.matcher(st2);
                    m1 = p1.matcher(st2);
                    if (m2.find()) {
                        break;
                    } else if (m1.find()) {
                        break;
                    } else
                        recoverArr.add(st2);
                }
            }
        }
        scanner.close();

        setTriggerList(recoverArr);
        long end = System.currentTimeMillis();
        Logger.log("Time: " + (end - start));
        return recoverArr;
    }

    /**
     *Setter method
     * @param list
     */
    public static void setTriggerList(List<String> list) {
        FileScanner.triggerList = (ArrayList<String>) list;
    }

    public static ArrayList<String> getTriggerList() {
        return triggerList;
    }

    /**
     * This method print the Trigger list
     * @param array
     */
    public static void printArray(List<String> array) {
        for (String anArray : array) {
            Logger.log(anArray);
        }
    }
}
