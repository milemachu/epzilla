/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.tests;

import jstm.core.Site;
import jstm.core.Transaction;

import org.junit.After;
import org.junit.Assert;

public class TransactionsTest {

    @After
    public void after() {
        Assert.assertEquals(null, Transaction.getCurrent());
        Site site = Site.getLocal();
        Assert.assertTrue(site.idle());
    }
}
