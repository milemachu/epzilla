package org.epzilla.accumulator.analytics;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Feb 28, 2010
 * Time: 11:41:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class StatCollector {
    // load at startup.
    private static StatCollector singleton = new StatCollector();

    private StatCollector() {

    }

    public static StatCollector getInstance() {
        return singleton;
    }

    // todo implement monitoring stuff.

}
