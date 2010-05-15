package org.epzilla.dispatcher.logs;

import org.epzilla.dispatcher.controlers.DispatcherUIController;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileScanner implements Runnable {
    private static List<String> recoverArr = new ArrayList<String>();
    private List<String> undoList = new ArrayList<String>();
    private File file;
    private static String strmatch = "";
    private static Scanner scanner = null;
    private static Matcher m1 = null;
    private static Matcher m2 = null;
    private static String st1 = "";
    private static String st2 = "";
    //	Pattern p1 = Pattern.compile("^(.{2}) (.*)$");
    private static Pattern p1 = Pattern.compile("^[CID0-9]+ (.{10})$");
    private static Pattern p2 = Pattern.compile("</commit>");

    public FileScanner(File dd, List<String> recArray) {
        this.file = dd;
        this.undoList = recArray;
    }

    public FileScanner() {

    }

    public void run() {
        for (int i = 0; i < undoList.size(); i++) {
            recoverArr.clear();
            readFile(file, undoList.get(i));

        }
    }

    public static List<String> readFile(File file, String strReq) {
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
            System.out.println("File not found");
        }
        printArray(recoverArr);
        long end = System.currentTimeMillis();
        System.out.println("Time: " + (end - start));
        return recoverArr;
    }

    public static void readFile(File file) {
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
//                       if (strmatch.equals(st)) {
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
//                       }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        printArray(recoverArr);
        long end = System.currentTimeMillis();
        System.out.println("Time: " + (end - start));
        DispatcherUIController.appendTriggers(recoverArr);
    }

    public static void printArray(List<String> array) {
        for (int i = 0; i < array.size(); i++) {
            System.out.println(array.get(i));
        }
    }
}
