package org.epzilla.dispatcher.dataManager;

import org.epzilla.dispatcher.controlers.DispatcherUIController;

/**
 * Created by IntelliJ IDEA.
 * User: PI-16
 * Date: Mar 11, 2010
 * Time: 8:34:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class EventsCounter {

    static int count=0;
    public EventsCounter(){
    }
    public static void setEventCount(){
        count++;
        String text = Integer.toString(count);
        DispatcherUIController.appendInEventsCount(text);
    }
    public static int getEventCount(){
        return count;
    }

}
