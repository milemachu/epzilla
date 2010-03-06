/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.tests.vm;

import java.util.ArrayList;

import jstm.tests.ClientTest;
import jstm.tests.Concurrency1;
import jstm.tests.VMTestBase;

import org.junit.Test;

public class DistributedVMTest extends VMTestBase {

    @Test
    public void runConflicting() throws InterruptedException {
        _conflicting = true;
        run();
    }

    @Test
    public void runNotConflicting() throws InterruptedException {
        _conflicting = false;
        run();
    }

    public void run() throws InterruptedException {
        System.out.println("Conflicting = " + _conflicting);

        start();

        createStructures();

        ArrayList<Thread> threads = new ArrayList<Thread>();

        System.out.println("Starting " + THREAD_COUNT + " threads, " + ClientTest.WRITE_COUNT + " writes");

        for (int i = 0; i < THREAD_COUNT; i++) {
            final int _i = i;

            Thread thread = new Thread("Client Thead " + i) {

                @Override
                public void run() {
                    DistributedVMTestClient client1 = new DistributedVMTestClient(DistributedVMTest.this, _i);
                    client1.run();
                }
            };

            threads.add(thread);
            thread.start();
        }

        while (_startedCount.get() < THREAD_COUNT)
            Thread.sleep(1);

        shareStructures();

        Concurrency1.finish(threads, ClientTest.WRITE_COUNT, _testCount);

        report();

        stop();
    }
}
