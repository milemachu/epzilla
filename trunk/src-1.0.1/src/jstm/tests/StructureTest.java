/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.tests;

import jstm.core.Site;
import jstm.core.Transaction;
import jstm.tests.generated.SimpleStructure;

import org.junit.Assert;
import org.junit.Test;

public class StructureTest extends TwoTransactionsTest {

    public StructureTest() {
    }

    @Test
    public void run() {
        a.tryToResume();

        SimpleStructure entity = new SimpleStructure();

        entity.setText("A");
        entity.setDouble(new Double(1));
        SimpleStructure entityForA = new SimpleStructure();
        entity.setEntity(entityForA);

        Assert.assertEquals("A", entity.getText());
        Assert.assertEquals(1.0, entity.getDouble().doubleValue());
        Assert.assertEquals(entityForA, entity.getEntity());

        a.suspend();

        //

        b.tryToResume();

        Assert.assertEquals(null, entity.getText());
        Assert.assertEquals(null, entity.getDouble());
        Assert.assertEquals(null, entity.getEntity());

        entity.setText("B");
        entity.setDouble(new Double(2));
        SimpleStructure entityForB = new SimpleStructure();
        entity.setEntity(entityForB);

        Assert.assertEquals("B", entity.getText());
        Assert.assertEquals(new Double(2), entity.getDouble());
        Assert.assertEquals(entityForB, entity.getEntity());

        b.suspend();

        //

        a.tryToResume();

        Assert.assertEquals("A", entity.getText());
        Assert.assertEquals(1.0, entity.getDouble().doubleValue());
        Assert.assertEquals(entityForA, entity.getEntity());

        a.beginCommit(null);

        Assert.assertEquals(Transaction.Status.ABORTED, b.getStatus());
        b.tryToResume();
        Assert.assertEquals(Transaction.Status.ABORTED, b.getStatus());

        Transaction c = Site.getLocal().startTransaction();

        Assert.assertEquals("A", entity.getText());
        Assert.assertEquals(1.0, entity.getDouble().doubleValue());
        Assert.assertEquals(entityForA, entity.getEntity());

        c.abort();
    }
}
