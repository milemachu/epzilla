/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.tests;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import jstm.core.*;
import jstm.core.Transaction.AbortedException;
import jstm.misc.Debug;
import jstm.tests.generated.SimpleObjectModel;
import jstm.tests.generated.SimpleStructure;
import jstm.tests.methods.SimpleStructureImpl;
import jstm.transports.clientserver.vm.VMServer;

public class VMTestBase extends TransactionsTest {

    public static final int THREAD_COUNT = 1;

    protected VMServer _server;

    protected Share _share;

    protected Site.InternalListener _listener;

    protected SimpleStructure[] _structures;

    protected final AtomicInteger _startedCount = new AtomicInteger();

    protected boolean _conflicting = false;

    protected final AtomicInteger _testCount = new AtomicInteger();

    protected final AtomicInteger _abortCount = new AtomicInteger();

    public final VMServer getServer() {
        return _server;
    }

    public boolean getConflicting() {
        return _conflicting;
    }

    public void started(ClientTest client) {
        _startedCount.incrementAndGet();
    }

    public void start() {
        _server = new VMServer();

        Site.getLocal().registerObjectModel(new SimpleObjectModel());

        try {
            _server.start();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        _share = new Share();

        {
            Transaction transaction = Site.getLocal().startTransaction();
            _server.getServerAndClients().getOpenShares().add(_share);

            try {
                transaction.commit();
            } catch (AbortedException e) {
                e.printStackTrace();
            }
        }

        // Listen for commits

        _listener = new Site.InternalListener() {

            public void onCommitting(Transaction transaction) {
            }

            public void onCommitted(Transaction transaction, long resultingCommitCount) {
                VMTestBase.this.onCommitted(transaction, resultingCommitCount);
            }

            public void onAborted(Transaction transaction) {
                VMTestBase.this.onAborted(transaction);
            }
        };

        Site.getLocal().addInternalListener(_listener);
    }

    protected void onCommitted(Transaction transaction, long resultingCommitCount) {
        Debug.assertion(Transaction.getCurrent() == transaction);

        if (transaction.getPrivateObjects() != null) {
            for (TransactedObject o : transaction.getPrivateObjects().keySet()) {
                if (o instanceof SimpleStructure && ((SimpleStructure) o).getInt() != null) {
                    int value = ((SimpleStructure) o).getInt();
                    int ref = _testCount.getAndIncrement();

                    if (_conflicting)
                        if (value != ref)
                            Debug.assertion(false, "" + value + " != " + ref);

                    Debug.log(2, transaction + ": int == " + Integer.toString(ref) + ", resulting commit count: " + resultingCommitCount);
                }
            }
        }
    }

    protected void onAborted(Transaction transaction) {
        Debug.assertion(Transaction.getCurrent() == transaction);
        _abortCount.incrementAndGet();
    }

    public void createStructures() {
        if (_conflicting)
            _structures = new SimpleStructure[1];
        else
            _structures = new SimpleStructure[THREAD_COUNT];

        for (int i = 0; i < _structures.length; i++) {
            Transaction transaction = Site.getLocal().startTransaction();

            _structures[i] = new SimpleStructureImpl();
            _structures[i].setText("Structure " + i);

            transaction.commit();
        }
    }

    public void shareStructures() {
        Transaction transaction = Site.getLocal().startTransaction();

        for (SimpleStructure structure : _structures)
            _share.add(structure);

        transaction.beginCommit(null);
        Debug.assertion(transaction.getStatus() == Transaction.Status.COMMITTED);
    }

    public void stop() {
        Site.getLocal().removeInternalListener(_listener);

        {
            Transaction transaction = Site.getLocal().startTransaction();
            _server.getServerAndClients().getOpenShares().remove(_share);

            try {
                transaction.commit();
            } catch (AbortedException e) {
                e.printStackTrace();
            }
        }

        _server.stop();
    }

    public void report() {
        int total = 0;

        for (SimpleStructure structure : _structures) {
            System.out.println("End value for " + structure.getText() + ": " + structure.getInt());
            total += structure.getInt();
        }

        long version = Site.getLocal().getVersionNumber();

        System.out.println("End values total: " + total + ", resultingCommitCount: " + version);

        if (!_conflicting) {
            // (Impossible to test _conflicting case because some transactions
            // are aborted client side)
            Debug.assertion(_abortCount.get() == 0);
            Debug.assertion(_testCount.get() == THREAD_COUNT * ClientTest.WRITE_COUNT);
        }

        // Don't test _conflicting case because some transactions are aborted
        // client side
    }
}
