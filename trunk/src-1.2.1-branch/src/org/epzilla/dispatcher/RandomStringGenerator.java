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
package org.epzilla.dispatcher;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: Feb 21, 2010
 * Time: 8:48:56 AM
 * To change this template use File | Settings | File Templates.
 */

// Class for testing only

public class RandomStringGenerator {

    private static int wordCount = 0;
//    private static int charCount = 100; //String memory usage (bytes) = 8 * (int) ((((no chars) * 2) + 45) / 8)
    private static String[] objects;
    private static final char[] symbols = new char[36];
    private static final Random random = new Random();
    private static char[] buf=new char[20];
    private static int currentPos = 0;

    static {
        for (int idx = 0; idx < 10; ++idx)
            symbols[idx] = (char) ('0' + idx);
        for (int idx = 10; idx < 36; ++idx)
            symbols[idx] = (char) ('a' + idx - 10);
    }


    public static void generate(int words) {
        wordCount = words;
        objects = new String[wordCount];
//        buf = new char[charCount];
        for (int i = 0; i < wordCount; i++) {
            objects[i] = nextString();
        }
    }

    public String getRandomString() {
        String temp ="";
        if (currentPos < objects.length) {
            temp = objects[currentPos];
            currentPos++;
        } else
            currentPos = 0;

        return temp;
    }

    public static String nextString() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
    }
}
