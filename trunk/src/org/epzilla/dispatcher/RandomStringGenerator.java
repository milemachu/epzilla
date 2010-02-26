package org.epzilla.dispatcher;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: Feb 21, 2010
 * Time: 8:48:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class RandomStringGenerator {

    private static int wordCount = 0;
//    private static int charCount = 100; //String memory usage (bytes) = 8 * (int) ((((no chars) * 2) + 45) / 8)
    private static String[] objects;
    private static final char[] symbols = new char[36];
    private static final Random random = new Random();
    private static char[] buf=new char[100];
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
