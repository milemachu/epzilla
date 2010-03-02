package Dispatcher;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: Feb 12, 2010
 * Time: 3:28:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class objectEngine {

    private int wordCount = 0;
    private int charCount = 49000; //String memory usage (bytes) = 8 * (int) ((((no chars) * 2) + 45) / 8)
    private String[] objects;
    private static final char[] symbols = new char[36];
    private final Random random = new Random();
    private final char[] buf;
    private int currentPos = 0;

    static {
        for (int idx = 0; idx < 10; ++idx)
            symbols[idx] = (char) ('0' + idx);
        for (int idx = 10; idx < 36; ++idx)
            symbols[idx] = (char) ('a' + idx - 10);
    }


    public objectEngine(int words) {
        wordCount = words;
        objects = new String[wordCount];
        buf = new char[charCount];
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

    public String nextString() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
    }
}
