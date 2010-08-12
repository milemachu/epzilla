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
package org.epzilla.accumulator.notificationSys;

import org.epzilla.accumulator.userinterface.AccumulatorUIControler;

import java.util.TimerTask;

/**
 * Created by IntelliJ IDEA.
 * This class use to manage the notifications and keep a count of accumulated notifications.
 * Author: Chathura
 * Date: May 18, 2010
 * Time: 11:12:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class NotificationManager {
    public static int count = 0;
    static TimerTaskN timer = new TimerTaskN();

    public NotificationManager() {
    }

    public static void setAlertCount() {
        count++;
    }
}

class TimerTaskN {

    public TimerTaskN() {
        final java.util.Timer timer1 = new java.util.Timer();
        int INITIAL_TIME_INTERVAL = 10000;
        int UPDATE_TIME_INTERVAL = 10000;
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                String text = Integer.toString(NotificationManager.count);
                AccumulatorUIControler.appendEventprocessed(text);
                System.gc();
            }
        }, INITIAL_TIME_INTERVAL, UPDATE_TIME_INTERVAL);


    }

}
