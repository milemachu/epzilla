package net.epzilla.accumulator.global;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Feb 28, 2010
 * Time: 11:40:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class DerivedEvent extends Event  {
    private ArrayList<Long> sourceEvents = new ArrayList<Long>();

    public ArrayList<Long> getSourceEvents() {
        return sourceEvents;
    }

    public void addSourceEventDescription(long sourceEvent) {
        this.sourceEvents.add(sourceEvent);
    }
}
