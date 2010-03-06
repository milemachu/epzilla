/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.tests;

import jstm.core.Site;
import jstm.core.TransactedMap;
import jstm.core.Transaction;

import org.junit.Assert;
import org.junit.Test;

public class MapTest extends TwoTransactionsTest {

    @Test
    public void run() {
        a.tryToResume();

        TransactedMap<Integer, String> map = new TransactedMap<Integer, String>();

        map.put(0, "A");
        map.put(1, "B");

        Assert.assertEquals("A", map.get(0));
        Assert.assertEquals("B", map.get(1));

        a.suspend();

        //

        b.tryToResume();

        Assert.assertEquals(null, map.get(0));
        Assert.assertEquals(null, map.get(1));

        map.put(0, "X");
        map.put(1, "Y");

        Assert.assertEquals("X", map.get(0));
        Assert.assertEquals("Y", map.get(1));

        b.suspend();

        //

        a.tryToResume();

        Assert.assertEquals("A", map.get(0));
        Assert.assertEquals("B", map.get(1));

        a.beginCommit(null);

        Assert.assertEquals(Transaction.Status.ABORTED, b.getStatus());
        b.tryToResume();
        Assert.assertEquals(Transaction.Status.ABORTED, b.getStatus());

        Transaction c = Site.getLocal().startTransaction();

        Assert.assertEquals("A", map.get(0));
        Assert.assertEquals("B", map.get(1));

        c.abort();
    }
}
