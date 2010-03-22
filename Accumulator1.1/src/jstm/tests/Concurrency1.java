/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.tests;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import jstm.core.Site;
import jstm.core.Transaction;
import jstm.tests.generated.SimpleStructure;

import org.junit.Assert;
import org.junit.Test;

public class Concurrency1 extends TransactionsTest {

    private final AtomicInteger _testCount = new AtomicInteger();

    private final SimpleStructure _entity = new SimpleStructure();

    private volatile boolean _start;

    @Test
    public void run() throws InterruptedException {
        ArrayList<Thread> threads = new ArrayList<Thread>();

        final int threadCount = 10, writeCount = 10000;

        System.out.println("Starting " + threadCount + " threads, " + writeCount + " writes");

        for (int i = 0; i < threadCount; i++) {
            final int _i = i;

            Thread thread = new Thread("Thread " + i) {

                @Override
                public void run() {
                    try {
                        runWriter(_i, writeCount);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            };

            threads.add(thread);
            thread.start();
        }

        // Listen for commits

        Site.InternalListener listener = new Site.InternalListener() {

            public void onCommitting(Transaction transaction) {
            }

            public void onCommitted(Transaction transaction, long resultingCommitCount) {
                Assert.assertEquals(Transaction.getCurrent(), transaction);
                int ref = _testCount.getAndIncrement();
//                Assert.assertEquals(_entity.getInt(), ref );
            }

            public void onAborted(Transaction transaction) {
                Assert.assertEquals(Transaction.getCurrent(), transaction);
            }
        };

        Site.getLocal().addInternalListener(listener);

        _start = true;

        finish(threads, writeCount, _testCount);

        Site.getLocal().removeInternalListener(listener);
    }

    public static void finish(ArrayList<Thread> threads, int count, AtomicInteger testCount) throws InterruptedException {
        long start = System.nanoTime();

        for (Thread thread : threads)
            thread.join();

        double writePerSec = (threads.size() * count) * 1e9 / (System.nanoTime() - start);
        double successPerSec = testCount.get() * 1e9 / (System.nanoTime() - start);
        System.out.println((int) writePerSec + " writes/s, " + (int) successPerSec + " successes/s");
    }

    private void runWriter(final int number, int count) throws InterruptedException {
        while (!_start)
            Thread.sleep(1);

        Site.getLocal().allowThread();

        for (int i = 0; i < count; i++) {
            final Transaction transaction = Site.getLocal().startTransaction();

            if (_entity.getInt() == null)
                _entity.setInt(0);
            else
                _entity.setInt(_entity.getInt().intValue() + 1);

            transaction.beginCommit(null);
        }
    }
}
