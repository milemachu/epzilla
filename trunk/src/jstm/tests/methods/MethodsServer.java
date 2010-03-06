/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.tests.methods;

import java.util.ArrayList;

import jstm.tests.ClientTest;
import jstm.tests.Concurrency1;
import jstm.tests.VMTestBase;

import org.junit.Test;

public class MethodsServer extends VMTestBase {

    @Test
    public void run() throws InterruptedException {
        _conflicting = false;

        start();

        // Launch clients

        ArrayList<Thread> threads = new ArrayList<Thread>();

        System.out.println("Starting " + THREAD_COUNT + " threads, " + ClientTest.WRITE_COUNT + " writes");

        for (int i = 0; i < THREAD_COUNT; i++) {
            final int _i = i;

            Thread thread = new Thread("Client Thead " + i) {

                @Override
                public void run() {
                    MethodsClient client1 = new MethodsClient(MethodsServer.this, _i);
                    client1.run();
                }
            };

            threads.add(thread);
            thread.start();
        }

        while (_startedCount.get() < THREAD_COUNT)
            Thread.sleep(1);

        createStructures();
        shareStructures();

        Concurrency1.finish(threads, ClientTest.WRITE_COUNT, _testCount);

        report();

        stop();
    }
}
