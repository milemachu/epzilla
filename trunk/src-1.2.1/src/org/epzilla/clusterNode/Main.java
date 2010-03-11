package org.epzilla.clusterNode;

import java.util.HashSet;
import java.util.ArrayList;

import org.epzilla.clusterNode.query.QuerySyntaxException;
import org.epzilla.clusterNode.query.Query;
import org.epzilla.clusterNode.parser.QueryParser;
import org.epzilla.clusterNode.parser.QueryExecuter;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Mar 8, 2010
 * Time: 1:34:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String[] args) throws QuerySyntaxException {
        HashSet<Query> set = new HashSet<Query>();
        QueryParser qp = new QueryParser();

        System.out.println("Triggers:");
        System.out.println("");
        for (int i = 0; i < 100; i++) {
            String tr = EventTriggerGenerator.getNextTrigger();
            System.out.println(tr);
            set.add(qp.parseQuery(tr));
        }

        System.out.println("");
        System.out.println("......................");

        QueryExecuter qe = new QueryExecuter();
        for (Query q : set) {
            qe.addQuery(q);
        }
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
        for (String str : events) {
            System.out.println("processed:\n" + qe.processEvents(str));
        }

    }


}
