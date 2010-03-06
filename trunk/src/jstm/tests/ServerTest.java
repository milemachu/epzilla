/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.tests;

import jstm.core.*;
import jstm.tests.generated.SimpleObjectModel;
import jstm.tests.generated.SimpleStructure;
import jstm.transports.clientserver.Server;

public class ServerTest {

    public static void Test(Server server) {
        System.out.println("Starting server share");

        Site.getLocal().registerObjectModel(new SimpleObjectModel());

        Share share = new Share();

        {
            Transaction transaction = Site.getLocal().startTransaction();
            server.getServerAndClients().getOpenShares().add(share);
            SimpleStructure structure = new SimpleStructure();
            structure.setText("Structure 0");
            share.add(structure);
            transaction.commit();
        }

        share.addListener(new Share.Listener<TransactedObject>() {

            public void onAdded(Transaction transaction, TransactedObject object) {
                System.out.println("Object added: " + object + " by " + transaction);
            }

            public void onRemoved(Transaction transaction, TransactedObject object) {
                System.out.println("Object removed: " + object + " by " + transaction);
            }
        });

        Site.getLocal().addInternalListener(new Site.InternalListener() {

            public void onCommitting(Transaction transaction) {
                // if (transaction.getPrivateObjects() != null) {
                // for (TransactedObject.Version version :
                // transaction.getPrivateObjects().values())
                // System.out.println(version.toString() + " by " +
                // transaction);
                // }
            }

            public void onCommitted(Transaction transaction, long resultingCommitCount) {
            }

            public void onAborted(Transaction transaction) {
            }
        });
    }
}
