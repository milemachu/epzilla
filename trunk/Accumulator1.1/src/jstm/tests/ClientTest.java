/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.tests;

import java.io.IOException;

import jstm.core.*;
import jstm.tests.generated.SimpleStructure;
import jstm.transports.clientserver.Client;
import jstm.transports.clientserver.ConnectionInfo;

public class ClientTest implements Runnable {

    public static final int WRITE_COUNT = 1000;

    protected Client _client;

    protected Site _site;

    protected int _number;

    protected Share _share;

    protected String _structureName;

    public ClientTest() {
    }

    protected void onStart() {
    }

    protected void onRun(SimpleStructure structure) throws InterruptedException {
        // structure.addListener(new FieldListener() {
        //
        // public void onChange(Transaction transaction, int fieldIndex) {
        // if(fieldIndex == SimpleStructure.TEXT_INDEX)
        // System.out.println("Text changed");
        // }
        // });

        Transaction transaction = null;

        for (int i = 0; i < WRITE_COUNT; i++) {
            transaction = _site.startTransaction();

            if (structure.getInt() == null)
                structure.setInt(0);
            else
                structure.setInt(structure.getInt().intValue() + 1);

            // if (_client instanceof VMClient) {
            // while (true) {
            // if (((VMClient) _client).canSendToServer())
            // break;
            //
            // Thread.sleep(1);
            // }
            // }

            // Debug.log(2, "Starting " + transaction.getInfo());

            transaction.beginCommit(null);
        }

        while (transaction.getStatus() != Transaction.Status.COMMITTED && transaction.getStatus() != Transaction.Status.ABORTED)
            Thread.sleep(10);
    }

    protected void onFinished() {
    }

    public void run() {
        try {
            ConnectionInfo connection;

            try {
                connection = _client.connect();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            _share = (Share) connection.getServerAndClients().getOpenShares().toArray()[0];

            onStart();

            SimpleStructure entity = null;

            if (_structureName == null)
                _structureName = "Structure " + _number;

            while (entity == null) {
                for (TransactedObject o : _share) {
                    if (o instanceof SimpleStructure)
                        if (_structureName.equals(((SimpleStructure) o).getText()))
                            entity = (SimpleStructure) o;
                }

                Thread.sleep(1);
            }

            System.out.println(_structureName + " value starts at " + entity.getInt());

            onRun(entity);

            System.out.println(_structureName + " value ends at " + entity.getInt());

            onFinished();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
