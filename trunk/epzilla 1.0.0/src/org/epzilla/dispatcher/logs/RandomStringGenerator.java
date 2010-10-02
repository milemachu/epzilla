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
package org.epzilla.dispatcher.logs;

import org.epzilla.util.Logger;

import java.util.Random;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * Test class for generating Random Strings
 * Author: Chathura
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
