/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.tests.methods;

import jstm.core.MethodCallback;
import jstm.core.Site;
import jstm.core.Transaction;
import jstm.misc.Debug;
import jstm.tests.ClientTest;
import jstm.tests.generated.SimpleObjectModel;
import jstm.tests.generated.SimpleStructure;
import jstm.transports.clientserver.vm.VMClient;

public class MethodsClient extends ClientTest {

    private final MethodsServer _test;

    private final Site _site;

    private int _callbackCount;

    public MethodsClient(MethodsServer test, int number) {
        _test = test;

        _site = Site.createTestSite();
        _client = test.getServer().createClient(_site, number);
        _number = number;

        _site.registerObjectModel(new SimpleObjectModel());
    }

    @Override
    protected void onStart() {
        _test.started(this);
    }

    @Override
    protected void onFinished() {
        _test.getServer().remove((VMClient) _client);
    }

    @Override
    protected void onRun(final SimpleStructure structure) throws InterruptedException {
        Transaction transaction = null;

        for (int i = 0; i < ClientTest.WRITE_COUNT; i++) {
            transaction = _site.startTransaction();

            SimpleStructure arg = new SimpleStructure();

            structure.method("bla", arg, new MethodCallback<String>() {

                public void onResult(String result) {
                    Debug.assertion(structure.getInt().toString().equals(result));
                    _callbackCount++;
                }

                public void onTransactionAborted() {
                    _callbackCount++;
                }

                public void onException(String message) {
                    Debug.fail("");
                }
            });

            transaction.commit();
        }

        while (transaction.getStatus() != Transaction.Status.COMMITTED && transaction.getStatus() != Transaction.Status.ABORTED)
            Thread.sleep(10);
    }
}
