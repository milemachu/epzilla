package org.epzilla.clusterNode;

import java.util.ArrayList;

import org.epzilla.clusterNode.query.QuerySyntaxException;
import org.epzilla.clusterNode.parser.QueryExecuter;
import org.epzilla.util.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Mar 8, 2010
 * Time: 1:34:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    final static QueryExecuter qe = new QueryExecuter();

    public static void main(String[] args) throws QuerySyntaxException {

        Logger.log("Triggers:");
        Logger.log("");
        for (int i = 0; i < 100; i++) {
            String tr = EventTriggerGenerator.getNextTrigger();
            Logger.log(tr);
            qe.addQuery(tr);
        }

        Logger.log("");
        Logger.log("......................");


        ArrayList<String> events = new ArrayList<String>();
        Logger.log("Events:");
        Logger.log("");
        for (int i = 0; i < 10; i++) {
            String x = EventTriggerGenerator.getNextEvent();
            events.add(x);
            Logger.log(x);
        }

        Logger.log("...................");
        Logger.log("");
        Logger.log("");
        Logger.log("Results");
        Logger.log("");


        // not needed.
        Thread t = new Thread() {
            public void run() {
                for (int i = 0; i < 30; i++) {
                    try {
                        String tr = EventTriggerGenerator.getNextTrigger();
                        Logger.log(tr);
                        qe.addQuery(tr);
                    } catch (Exception e) {

                    }
                }
            }
        };
        t.start();
        for (String str : events) {
            Logger.log("processed:\n" + qe.processEvents(str));


        }

    }


}
