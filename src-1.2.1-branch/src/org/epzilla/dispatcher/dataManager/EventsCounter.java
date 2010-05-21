package org.epzilla.dispatcher.dataManager;

import org.epzilla.dispatcher.controlers.DispatcherUIController;

import java.util.TimerTask;

import jstm.transports.clientserver.socket.SocketClient;

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
    static TimerTaskC timer = new TimerTaskC();

    public EventsCounter() {
    }

    public static void setInEventCount() {
        countIn++;

    }

    public static void setOutEventCount() {
        countOut++;


    }
}

class TimerTaskC {

    public TimerTaskC() {
        final java.util.Timer timer1 = new java.util.Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                String text = Integer.toString(EventsCounter.countOut);
                DispatcherUIController.appendOutEventCount(text);
                text = Integer.toString(EventsCounter.countIn);
                DispatcherUIController.appendInEventsCount(text);
                System.out.println("count:" + text);
                System.gc();
            }
        }, 10, 10000);


    }

}