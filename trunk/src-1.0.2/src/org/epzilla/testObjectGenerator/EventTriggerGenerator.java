package org.epzilla.testObjectGenerator;

import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: Mar 7, 2010
 * Time: 9:01:49 AM
 * To change this template use File | Settings | File Templates.
 */
public class EventTriggerGenerator {
    private static final char[] symbols = new char[36];
    private static final Random random = new Random();
    private static char[] buf = new char[20];

    static {
        for (int idx = 0; idx < 10; ++idx)
            symbols[idx] = (char) ('0' + idx);
        for (int idx = 10; idx < 36; ++idx)
            symbols[idx] = (char) ('a' + idx - 10);
    }

    public static String nextString() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
    }

    //For teting only-Dishan
    public static void main(String[] args) {
        String temp;
        for (int i = 0; i < 10; i++) {
            temp = getNextTrigger();
        }
    }


    public static String getNextEvent() {
        StringBuilder writer = new StringBuilder();
        try {
            writer.append("Title");
            writer.append(',');
            writer.append("CarModel");
            writer.append(',');
            writer.append("Color");
            writer.append(',');
            writer.append("Year");
            writer.append('\n');

            writer.append("CarDetails");
            writer.append(',');
            writer.append(Const.WORDS[random.nextInt(Const.WORDS.length)]);
            writer.append(',');
            writer.append(Const.COLOR[random.nextInt(Const.COLOR.length)]);
            writer.append(',');
            writer.append(Const.YEAR[random.nextInt(Const.YEAR.length)]);
            writer.append('\n');

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return writer.toString();
    }

//    "select CarDetails.CarModel where CarDetails.Year=1980  output as Details"

    public static String getNextTrigger() {
        StringBuilder writer = new StringBuilder();
        try {
            writer.append("SELECT");
            writer.append(" ");
            writer.append("CarDetails");
            writer.append('.');
            writer.append(Const.PROPERTY[random.nextInt(Const.PROPERTY.length)]);
            writer.append(" ");
            writer.append("WHERE");
            writer.append(" ");
            writer.append("CarDetails");
            writer.append('.');
            int temp = random.nextInt(Const.PROPERTY.length);
            writer.append(Const.PROPERTY[temp]);
            writer.append('=');
            switch (temp) {
                case 0:
                    writer.append(Const.WORDS[random.nextInt(Const.WORDS.length)]);
                    break;
                case 1:
                    writer.append(Const.YEAR[random.nextInt(Const.YEAR.length)]);
                    break;
                case 2:
                    writer.append(Const.COLOR[random.nextInt(Const.COLOR.length)]);
                    break;
            }
            writer.append(" ");
            writer.append("OUTPUT AS");
            writer.append(" ");
            writer.append("CarDetails");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return writer.toString();
    }


}
