package org.epzilla.testObjectGenerator;

import org.epzilla.util.Logger;

import java.util.Random;

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
    private static int turn = 0;

    static {
        for (int idx = 0; idx < 10; ++idx)
            symbols[idx] = (char) ('0' + idx);
        for (int idx = 10; idx < 36; ++idx)
            symbols[idx] = (char) ('a' + idx - 10);
    }

    public static String nextString() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(stockSymbols.length)];
        return new String(buf);
    }

    //For testing only-Dishan

    public static void main(String[] args) {
        String temp;
        for (int i = 0; i < 1000; i++) {
            temp = getNextTrigger();
            Logger.log(temp);
        }
    }


    public static String getNextEvent() {
//        StringBuilder writer = new StringBuilder();
//        try {
//            writer.append("Title");
//            writer.append(',');
//            writer.append("CarModel");
//            writer.append(',');
//            writer.append("Color");
//            writer.append(',');
//            writer.append("Year");
//            writer.append('\n');
//
//            writer.append("CarDetails");
//            writer.append(',');
//            writer.append(Const.WORDS[random.nextInt(Const.WORDS.length)]);
//            writer.append(',');
//            writer.append(Const.COLOR[random.nextInt(Const.COLOR.length)]);
//            writer.append(',');
//            writer.append(Const.YEAR[random.nextInt(Const.YEAR.length)]);
//
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
        return generateStockEvent();
    }


    public static String generateStockEvent() {
        StringBuilder writer = new StringBuilder();
        try {
            int symIndex = (int) (Math.random() * (stockSymbols.length));


            writer.append(stockSymbols[symIndex]);
            writer.append('\n');

            // bid last amount ask
            int lim = attributes.length - 1;
            for (int i = 0; i < attributes.length; i++) {
                writer.append(attributes[i]);
                if (i < lim) {
                    writer.append(",");
                }
            }
            writer.append("\n");
            int r = (int) (Math.random() * 10);
            for (int i = 0; i < r; i++) {
                double bid = Math.random() * 150;
                writer.append(round(bid, 2)).append(",");
                if (Math.random() > 0.5) {
                    writer.append(round((bid + (Math.random() * 5)), 2));
                } else {
                    writer.append(round((bid - (Math.random() * 5)), 2));
                }
                writer.append(",");
                writer.append(((int) (Math.random() * 1000)) * 100);
                writer.append(",");
                writer.append(round((bid + (Math.random() * 5)), 2));

                writer.append("\n");
            }


        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return writer.toString();
    }


    private static double round(double input, int decimals) {
        int pow = (int) Math.pow(10, decimals);
        return ((((int) (input * pow)) * 1.0)) / pow;
    }
//    "select CarDetails.CarModel where CarDetails.Year=1980  output as Details"

    public static String getNextTrigger() {
//        StringBuilder writer = new StringBuilder();
//        Random random = new Random();
//        turn = random.nextInt(2);
//        try {
//            if (turn == 0) {
//                generateCarDetailsTrigger(writer);
//
//            } else {
//                generateBikeDetailsTrigger(writer);
//            }
//
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        return writer.toString();
        return generateStockDetailsTrigger();

    }

    private static void generateBikeDetailsTrigger(StringBuilder writer) {
        writer.append("SELECT");
        writer.append(" ");
        writer.append("BikeDetails");
        writer.append('.');
        writer.append(Const.BIKEROPERTY[random.nextInt(Const.BIKEROPERTY.length)]);
        writer.append(" ");
        writer.append("WHERE");
        writer.append(" ");
        writer.append("BikeDetails");
        writer.append('.');
        int temp = random.nextInt(Const.BIKEROPERTY.length);
        writer.append(Const.BIKEROPERTY[temp]);
        writer.append('=');
        switch (temp) {
            case 0:
                writer.append(Const.BIKETYPES[random.nextInt(Const.BIKETYPES.length)]);
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
        writer.append("BikeDetails");
    }

    private static void generateCarDetailsTrigger(StringBuilder writer) {
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

    static String[] stockSymbols = {"Dialog", "Mobitel", "Etisalat", "JohnKeells", "LankaIOC", "Sierra", "Janashakthi",
            "Dankotuwa", "DFCC", "AmanaTakaful", "Hayleys", "Hemas", "EastWest", "LankaCeramic", "LankaCement", "Nawaloka"
            , "PanAsia", "Seylan", "TokyoCement", "UnitedMotors", "Mahaweli", "LBFinance", "RichardPeiris"};

    static String[] operations = {"", "min", "max", "avg", "sum"};
    static String[] attributes = {"bidPrice", "last", "amount", "askPrice"};
            static String[] con = new String[] {"=","!=",">", "<", "<="};

    public static String generateStockDetailsTrigger() {
        StringBuilder writer = new StringBuilder("");
        writer.append("SELECT");
        writer.append(" ");

        int symIndex = (int) (Math.random() * (stockSymbols.length));
        int opIndex = (int) (Math.random() * (operations.length));
        int attIndex = (int) (Math.random() * (attributes.length));

        if (attIndex != 2) {
            if (opIndex == 4) {
                opIndex = 3;
            }
        } else {
            if (opIndex == 3) {
                opIndex = 4;
            }
        }

//        writer.append("StockTrades.min");
        if (!operations[opIndex].equals("")) {
            writer.append(operations[opIndex]);
            writer.append("(");
        }
        writer.append(stockSymbols[symIndex]);
        writer.append(".");
        writer.append(attributes[attIndex]);

        if (!operations[opIndex].equals("")) {
            writer.append(")");
        }

//        writer.append('.');
//        writer.append(Const.PROPERTY[random.nextInt(Const.PROPERTY.length)]);
        writer.append(" ");

        if (Math.random() > 0.5) {
            writer.append("WHERE ");

            writer.append(stockSymbols[symIndex]);
            writer.append(".");
            writer.append(attributes[attIndex]);
                           writer.append(" ");
            int ci = (int) ((con.length)*(Math.random()));
            writer.append(con[ci]);
            writer.append(" ");
            
            writer.append((int) (Math.random() * 100)) ;
            writer.append(" ");
            

        }
//        writer.append("WHERE");
//        writer.append(" ");
//        writer.append("CarDetails");
//        writer.append('.');
//        int temp = random.nextInt(Const.PROPERTY.length);
//        writer.append(Const.PROPERTY[temp]);
//        writer.append('=');
//        switch (temp) {
//            case 0:
//                writer.append(Const.WORDS[random.nextInt(Const.WORDS.length)]);
//                break;
//            case 1:
//                writer.append(Const.YEAR[random.nextInt(Const.YEAR.length)]);
//                break;
//            case 2:
//                writer.append(Const.COLOR[random.nextInt(Const.COLOR.length)]);
//                break;
//        }
//        writer.append(" ");
        writer.append("OUTPUT AS");
        writer.append(" ");
        writer.append(stockSymbols[symIndex]);
        writer.append("-notification");
//        writer.append("CarDetails");
        return writer.toString();
    }


    private static void generateTrigger(StringBuilder writer) {
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

}
