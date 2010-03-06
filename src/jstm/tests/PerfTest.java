/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.tests;

import java.util.HashMap;
import java.util.Random;

import jstm.core.Site;
import jstm.core.Transaction;
import jstm.misc.Utils;
import jstm.tests.generated.SimpleStructure;

import org.junit.Test;

public class PerfTest extends TransactionsTest {

    @Test
    public void run() {
        Transaction t = Site.getLocal().startTransaction();
        SimpleStructure entity = new SimpleStructure();
        entity.setText("A");

        run(entity);

        Object[] array = new Object[1000];
        HashMap<Object, Object> map = new HashMap<Object, Object>();

        for (int i = 0; i < array.length; i++) {
            array[i] = new Object();
            map.put(array[i], new Object());
        }

        run(array, map);

        long start = System.nanoTime();
        // run(entity);
        // run(array, map);
        // runUID();

        System.out.println("Perf test : " + (System.nanoTime() - start) / 1e6 + " ms");

        // try {
        // Thread.sleep(100000);
        // } catch (InterruptedException e) {
        // }

        t.abort();
    }

    private static void run(SimpleStructure entity) {
        for (int i = 0; i < (int) 1e6; i++) {
            entity.setText("A");

            // if (!entity.getText().equals("A"))
            // throw new RuntimeException();
        }
    }

    private static void run(Object[] array, HashMap<Object, Object> map) {
        Random rand = new Random();

        for (int i = 0; i < (int) 1e6; i++) {
            Object o = array[rand.nextInt(array.length)];
            map.get(o);

            // if (!entity.getText().equals("A"))
            // throw new RuntimeException();
        }
    }

    public static void runUID() {
        for (int i = 0; i < 1e6; i++) {
            // UUID.randomUUID();
            Utils.createUID();
            // System.out.println(id);
        }
    }
}
