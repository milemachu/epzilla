package org.epzilla.util;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Feb 26, 2010
 * Time: 7:35:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class Logger {
    private static boolean dolog = false;
    private static boolean doErrorlog = true;
    private static boolean[] keyMap = new boolean[10];

    public static void enableLoggingFor(int key) {
        keyMap[key] = true;
    }

    public static void setLogging(boolean enableLogging) {
        dolog = enableLogging;
    }

    public static void log(String string) {
        if (dolog) {
            System.out.println(string);
        }
    }

    public static void log(boolean input) {
        if (dolog) {
            System.out.println(input);
        }
    }


    
    public static void log(String input, int key) {
        if (keyMap[key]) {
            System.out.println(input);
        }
    }

    public static void log(Object obj) {
         if (dolog) {
            System.out.println(obj.toString());
        }
    }

    public static void error(String s, Exception e) {
        if (doErrorlog) {
            if (s != null) {
                System.out.println(s);
            }
            if (e != null)
                e.printStackTrace();
        }
    }
}
