package org.epzilla.clusterNode;

import java.util.ArrayList;

import org.epzilla.clusterNode.query.QuerySyntaxException;
import org.epzilla.clusterNode.parser.QueryExecuter;

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

        System.out.println("Triggers:");
        System.out.println("");
        for (int i = 0; i < 100; i++) {
            String tr = EventTriggerGenerator.getNextTrigger();
            System.out.println(tr);
            qe.addQuery(tr);
        }

        System.out.println("");
        System.out.println("......................");


        ArrayList<String> events = new ArrayList<String>();
        System.out.println("Events:");
        System.out.println("");
        for (int i = 0; i < 10; i++) {
            String x = EventTriggerGenerator.getNextEvent();
            events.add(x);
            System.out.println(x);
        }

        System.out.println("...................");
        System.out.println("");
        System.out.println("");
        System.out.println("Results");
        System.out.println("");


        // not needed.
        Thread t = new Thread() {
            public void run() {
                for (int i = 0; i < 30; i++) {
                    try {
                        String tr = EventTriggerGenerator.getNextTrigger();
                        System.out.println(tr);
                        qe.addQuery(tr);
                    } catch (Exception e) {

                    }
                }
            }
        };
        t.start();
        for (String str : events) {
            System.out.println("processed:\n" + qe.processEvents(str));


        }

    }


}
