package net.epzilla.node.parser;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: May 24, 2010
 * Time: 6:14:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class BlankProcessor extends QueryExecutor {

    @Override
    public String processEvents(String events) {
//        return super.processEvents(events);    //To change body of overridden methods use File | Settings | File Templates.
        return events;
    }
}
