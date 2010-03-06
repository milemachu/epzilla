/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.tests;

import jstm.core.Site;
import jstm.core.Transaction;

import org.junit.Before;

public class TwoTransactionsTest extends TransactionsTest {

    protected Transaction a, b;

    @Before
    public void before() {
        Site.getLocal().allowThread();
        
        a = Site.getLocal().startTransaction();
        a.suspend();

        b = Site.getLocal().startTransaction();
        b.suspend();
    }
}
