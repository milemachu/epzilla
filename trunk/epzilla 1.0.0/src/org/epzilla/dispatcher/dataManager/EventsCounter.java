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
package org.epzilla.dispatcher.dataManager;

import org.epzilla.dispatcher.controlers.DispatcherUIController;

import java.util.TimerTask;

/**
 * Created by IntelliJ IDEA.
 * This class is to take count of incom,ing and outgoing event count from the Dispathcer
 * Author: Chathura
 * Date: May 2, 2010
 * Time: 8:34:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class EventsCounter {

    public static int countIn = 0;
    public static int countOut = 0;
    public static int lastOut = 0;
    static TimerTaskC timer = new TimerTaskC();
    public static int dispatchRate;
    public static int maxDispatchRate = 1000;

    public EventsCounter() {
    }
    /*
    set incoming event count
     */

    public static void setInEventCount() {
        countIn++;
    }
    /*
   set outgoing event count
    */

    public static void setOutEventCount() {
        countOut++;
    }

    public static void setEventDispatchRate(int rate) {
        dispatchRate = rate;
    }

    public static int getEventDispatchRate() {
        return dispatchRate;
    }

    public static void setMaxRate(int mRate) {
        maxDispatchRate = mRate;
    }

    public static int getMaxRate() {
        return maxDispatchRate;
    }
}
/*
Timer class to periodically get the event dispatching rate
*/

class TimerTaskC {
    private int UPDATE_SERVICE_RUNNING_TIME = 10000;
    private int INITIAL_START_TIME = 10;

    public TimerTaskC() {
        final java.util.Timer timer1 = new java.util.Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                int rate = (EventsCounter.countOut - EventsCounter.lastOut) / 10;
                if (rate > EventsCounter.getMaxRate()) {
                    EventsCounter.setMaxRate(rate);
                }
                String textRate;
                textRate = Integer.toString(rate);
                EventsCounter.setEventDispatchRate(rate);
                DispatcherUIController.setEDRate(textRate);
                String text = Integer.toString(EventsCounter.countOut);
                DispatcherUIController.appendOutEventCount(text);
                text = Integer.toString(EventsCounter.countIn);
                DispatcherUIController.appendInEventsCount(text);
                EventsCounter.lastOut = EventsCounter.countOut;
                System.gc();
            }
        }, INITIAL_START_TIME, UPDATE_SERVICE_RUNNING_TIME);


    }

}
