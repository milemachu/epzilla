package org.epzilla.accumulator.global;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Feb 28, 2010
 * Time: 11:40:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class DerivedEvent extends Event {
    private ArrayList<Long> sourceEvents = null;
    private long srcId;

    public long getSrcId() {
        return srcId;
    }

    public void setSrcId(long srcId) {
        this.srcId = srcId;
    }

    public DerivedEvent() {

    }

    public ArrayList<Long> getSourceEvents() {
        return sourceEvents;
    }

    public void addSourceEventDescription(long sourceEvent) {
        if (sourceEvents == null) {
            sourceEvents = new ArrayList<Long>();
        }
        this.sourceEvents.add(sourceEvent);
    }
}
