package org.epzilla.accumulator.notificationSys;

import org.epzilla.accumulator.userinterface.AccumulatorUIControler;

import java.util.TimerTask;

/**
 * Created by IntelliJ IDEA.
 * User: Randika
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
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                String text = Integer.toString(NotificationManager.count);
                AccumulatorUIControler.appendEventprocessed(text);
                System.gc();
            }
        }, 10000, 10000);


    }

}