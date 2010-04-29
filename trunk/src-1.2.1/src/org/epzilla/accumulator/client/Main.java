package org.epzilla.accumulator.client;


import java.util.ArrayList;
import java.rmi.Naming;

import org.epzilla.accumulator.client.parser.QueryExecuter;
import org.epzilla.accumulator.client.query.QuerySyntaxException;
import org.epzilla.accumulator.global.DerivedEvent;
import org.epzilla.accumulator.service.AccumulatorService;
import org.epzilla.accumulator.util.OpenSecurityManager;


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

        try {
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new OpenSecurityManager());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("Triggers:");
        System.out.println("");
        for (int i = 0; i < 100; i++) {
            String tr = EventTriggerGenerator.getNextTrigger();
            tr = tr + ".xy";
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
        String derivedEventString = null;
        int id = 0;
        for (String str : events) {
            try {
                derivedEventString = qe.processEvents(str);
                System.out.println("processed:\n" + derivedEventString);

                AccumulatorService serv = (AccumulatorService) Naming.lookup("rmi://127.0.0.1:1099/AccumulatorService");
                DerivedEvent de = new DerivedEvent();
                de.setContent(derivedEventString);
                de.setClientId(0);
                de.setEventId(id);
                serv.receiveDerivedEvent(de);
                try {
                        Thread.sleep(1000);
                }   catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {

            }
            id++;


        }

    }


}
