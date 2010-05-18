package net.epzilla.util;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Feb 26, 2010
 * Time: 7:35:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class Logger {
    private static boolean dolog = true;
    private static boolean doErrorlog = true;

    public static void setLogging(boolean enableLogging) {
        dolog = enableLogging;
    }

    public static void log(String string) {
        if (dolog) {
            System.out.println(string);
        }
    }

    public static void error(String s, Exception e) {
        if (doErrorlog) {
            if (s != null) {
                System.out.println(s);
            }
            e.printStackTrace();
        }
    }


}
