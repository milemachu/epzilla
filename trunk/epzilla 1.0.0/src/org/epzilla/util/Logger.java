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
package org.epzilla.util;

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
    private static boolean[] keyMap = new boolean[10];

    // values for conditional logging.
    public static int strat = 1;

    // set conditions here
    static {
        keyMap[strat] = true;
        keyMap[5] = true;

    }



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

    public static void log(Object input, int key) {
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
