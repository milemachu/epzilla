/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.tests;

import jstm.core.*;

import org.junit.Assert;
import org.junit.Test;

public class TransactedListTest1 extends TwoTransactionsTest {

    @Test
    public void run() {
        a.tryToResume();

        TransactedList<String> list = new TransactedList<String>();

        list.add("A");
        list.add("B");

        Assert.assertTrue(list.contains("A"));
        Assert.assertTrue(!list.isEmpty());

        a.suspend();

        //

        b.tryToResume();

        Assert.assertTrue(!list.contains("A"));
        Assert.assertTrue(list.isEmpty());

        list.add("X");
        list.add("Y");

        Assert.assertTrue(list.contains("X"));
        Assert.assertTrue(!list.isEmpty());

        b.suspend();

        //

        a.tryToResume();

        Assert.assertTrue(list.contains("A"));
        Assert.assertTrue(!list.isEmpty());

        a.beginCommit(null);

        Assert.assertEquals(b.getStatus(), Transaction.Status.ABORTED);
        b.tryToResume();
        Assert.assertEquals(b.getStatus(), Transaction.Status.ABORTED);

        Transaction c = Site.getLocal().startTransaction();

        Assert.assertTrue(list.contains("A"));
        Assert.assertTrue(!list.isEmpty());

        c.abort();
    }
}
