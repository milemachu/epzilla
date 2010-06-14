package org.epzilla.accumulator.dataManager;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: May 23, 2010
 * Time: 12:28:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class Event {
    private String eventID="";
    private ArrayList segments = new ArrayList();

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public void addSegment(String segment)
    {
        segments.add(segment);
    }

    public int getSegmentCount()
    {
      return segments.size();  
    }

}
