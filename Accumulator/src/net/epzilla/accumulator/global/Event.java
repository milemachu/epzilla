package net.epzilla.accumulator.global;

import java.io.Serializable;


public class Event implements Serializable {

    private String content = null;
    private long clientId;
    private long eventId;

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

}
