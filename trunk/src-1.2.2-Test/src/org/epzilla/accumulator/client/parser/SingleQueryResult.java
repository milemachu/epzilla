package org.epzilla.accumulator.client.parser;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Mar 10, 2010
 * Time: 1:41:49 AM
 * To change this template use File | Settings | File Templates.
 */
public class SingleQueryResult {

    private String[] headers;
    private ArrayList<String[]> events = new ArrayList<String[]>();
    private boolean isEmpty = true;

    public ArrayList<String[]> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<String[]> events) {
        this.events = events;
    }

    public void addEvent(String[] event) {
        this.events.add(event);
        this.isEmpty = false;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public String[] getHeaders() {
        return headers;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }


}
