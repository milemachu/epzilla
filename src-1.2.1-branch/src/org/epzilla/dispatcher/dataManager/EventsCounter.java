package org.epzilla.dispatcher.dataManager;

import org.epzilla.dispatcher.controlers.DispatcherUIController;

import java.util.TimerTask;

/**
 * Created by IntelliJ IDEA.
 * User: chathura
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

    public static void setInEventCount() {
        countIn++;
    }

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
//                if (rate < 100) {
//                    textRate = "100";
//                    rate = 100;
//                } else {
//                    textRate = Integer.toString(rate);
//                }
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