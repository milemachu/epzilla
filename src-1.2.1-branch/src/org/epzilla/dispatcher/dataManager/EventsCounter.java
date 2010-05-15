package org.epzilla.dispatcher.dataManager;

import org.epzilla.dispatcher.controlers.DispatcherUIController;

/**
 * Created by IntelliJ IDEA.
 * User: chathura
 * Date: May 2, 2010
 * Time: 8:34:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class EventsCounter {

    private static int countIn = 0;
    private static int countOut = 0;

    public EventsCounter() {
    }

    public static void setInEventCount(int i) {
        countIn = countIn + i;
        String text = Integer.toString(countIn);
        DispatcherUIController.appendInEventsCount(text);
    }

    public static void setOutEventCount(int j) {
        countOut = countOut + j;
        String text = Integer.toString(countOut);
        DispatcherUIController.appendOutEventCount(text);
    }
}
