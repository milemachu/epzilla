package org.epzilla.dispatcher.rmi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;


public class TriggerRepresentation implements Serializable {
    private String trigger;
    private String clientId;
             private String triggerId;
    private String dispatcherId;

    public String getTriggerId() {
        return triggerId;
    }

    public void setTriggerId(String triggerId) {
        this.triggerId = triggerId;
    }

    public String getDispatcherId() {
        return dispatcherId;
    }

    public void setDispatcherId(String dispatcherId) {
        this.dispatcherId = dispatcherId;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

}
