package net.epzilla.accumulator.global;

import java.io.Serializable;


public class SourceEvent extends Event {
    // todo additional attributes.
    private String dispatcherId;

    public String getDispatcherId() {
        return dispatcherId;
    }

    public void setDispatcherId(String dispatcherId) {
        this.dispatcherId = dispatcherId;
    }
}
