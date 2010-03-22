/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.tests;

import java.io.IOException;
import java.util.ArrayList;

import jstm.core.*;
import jstm.tests.generated.SimpleObjectModel;
import jstm.transports.clientserver.ConnectionInfo;
import jstm.transports.clientserver.vm.VMClient;
import jstm.transports.clientserver.vm.VMServer;

import org.junit.Test;

@SuppressWarnings("unchecked")
public class TransactedListTest3 extends TransactedListTestBase {

    private final ThreadLocal<Session> _session = new ThreadLocal<Session>();

    private final ArrayList<Thread> _threads = new ArrayList<Thread>();

    private VMServer _server;

    public TransactedListTest3() {
    }

    @Test
    public void test() {
        _server = new VMServer();

        Site.getLocal().registerObjectModel(new SimpleObjectModel());

        try {
            _server.start();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        Share share = new Share();

        {
            Transaction transaction = Site.getLocal().startTransaction();
            _server.getServerAndClients().getOpenShares().add(share);
            transaction.commit();
        }

        for (int i = 0; i < 2; i++) {
            Thread thread = new Session(i);
            _threads.add(thread);
            thread.start();
        }

        for (Thread thread : _threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
            }
        }

        _threads.clear();

        {
            Transaction transaction = Site.getLocal().startTransaction();
            _server.getServerAndClients().getOpenShares().remove(share);
            transaction.commit();
        }

        _server.stop();

        System.out.println(_commitCount.get() + " commits");
    }

    @Override
    protected Site getSite() {
        return _session.get().Site;
    }

    @Override
    protected void onListCreated(TransactedList list) {
        Transaction transaction = null;

        if ((getRun() & ONE_TRANSACTION) == 0)
            transaction = getSite().startTransaction();

        _session.get().Share.add(list);

        if (transaction != null)
            transaction.commit();
    }

    @Override
    protected void onListDisposed(TransactedList list) {
        Transaction transaction = null;

        if ((getRun() & ONE_TRANSACTION) == 0)
            transaction = getSite().startTransaction();

        _session.get().Share.remove(list);

        if (transaction != null)
            transaction.commit();
    }

    private final class Session extends Thread {

        public final Site Site = jstm.core.Site.createTestSite();

        private final int _i;

        public Share Share;

        public Session(int i) {
            _i = i;
        }

        @Override
        public void run() {
            VMClient client = TransactedListTest3.this._server.createClient(Site, _i);

            ConnectionInfo connection = null;

            try {
                connection = client.connect();
            } catch (IOException e) {
            }

            Share = (Share) connection.getServerAndClients().getOpenShares().toArray()[0];

            TransactedListTest3.this._session.set(this);
            TransactedListTest3.this.run();
            TransactedListTest3.this._session.set(null);

            _server.remove(client);
        }
    }
}
