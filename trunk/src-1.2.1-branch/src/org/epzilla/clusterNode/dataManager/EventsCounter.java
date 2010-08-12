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
package org.epzilla.clusterNode.dataManager;

import org.epzilla.clusterNode.userInterface.NodeUIController;

import java.util.TimerTask;

/**
 * Created by IntelliJ IDEA.
 * This class is to take the incoming and outgoing event counts
 * Author: Chathura
 * Date: May 24, 2010
 * Time: 4:44:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventsCounter {
    public static int countIn = 0;
    static TimerTaskC timer = new TimerTaskC();

    public EventsCounter() {
    }

    public static void setInEventCount() {
        countIn++;
    }
}

class TimerTaskC {

    public TimerTaskC() {
        final java.util.Timer timer1 = new java.util.Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                String text = Integer.toString(EventsCounter.countIn);
                NodeUIController.setEventCount(text);
                System.gc();
            }
        }, 10, 10000);


    }
}
