package org.epzilla.dispatcher.logs;

import org.epzilla.util.Logger;

import java.util.Random;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: chathura
 * Date: Mar 25, 2010
 * Time: 8:48:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class RandomStringGenerator {
    static String token = "";
	static ArrayList<String> myArr = new ArrayList<String>();
    static int START = 1;
    static int END = 10;
    static int delay = 0000;   // delay for 0 sec.
    static int interval = 10000;  // iterate every sec.
    static Timer timer = new Timer();


	public static void main(String[] args) throws IOException {
		final WriteLog lf = new WriteLog();
		Logger.log("Generating random strings");
	    final Random random = new Random();
       /*

        */
        timer.scheduleAtFixedRate(new TimerTask()
        {
            public void run()
            {
                 for(int j = 0; j<3 ;j++){
	      generateRandomStrings(START, END, random);
                     try {
                         lf.writeInit(myArr,"1",""+j);
                     } catch (IOException e) {
                         e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                     }
                 }

	    Logger.log("Done.");
            }
        }, delay, interval);
        /*

         */

			}
	 private static void generateRandomStrings(int aStart, int aEnd, Random aRandom){
		    token = Long.toString(Math.abs(aRandom.nextLong()), 36);
		    myArr.add(token);
		    }
}
