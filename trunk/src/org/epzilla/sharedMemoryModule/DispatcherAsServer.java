package org.epzilla.sharedMemoryModule;

import jstm.transports.clientserver.Server;
import jstm.transports.clientserver.socket.SocketServer;
import jstm.core.*;

import java.io.IOException;

import org.epzilla.dispatcher.NodeVariables;
import org.epzilla.dispatcher.TriggerManager;
import org.epzilla.dispatcher.ClusterLeaderIpListManager;
import org.epzilla.dispatcher.DispatcherUIController;
import generatedObjectModels.triggerInfoObject;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: Feb 18, 2010
 * Time: 3:58:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class DispatcherAsServer {

    private static Share share;

    public static boolean startServer() {
        boolean success = false;
        DispatcherUIController.appendTextToStatus("Starting STM server on: " + NodeVariables.getNodeIP());
        Site.getLocal().registerObjectModel(new generatedObjectModels.dispatcherObjectModel());
        try {
            int port = NodeVariables.getPort();
            Server server = new SocketServer(port);
            server.start();
            DispatcherUIController.appendTextToStatus("Attaching a share to sites group: server and clients...");

            share = new Share();

            DispatcherUIController.appendTextToStatus("Waiting For Clients...");
            // Once connected, retrieve the Group that represents the
            // server and its
            // clients

            Group serverAndClientsSites = server.getServerAndClients();

            // Open a share in this group is there is none yet

            if (serverAndClientsSites.getOpenShares().size() == 0) {
                Transaction transaction = Site.getLocal().startTransaction();
                server.getServerAndClients().getOpenShares().add(share);
                transaction.commit();
            }

            share = (Share) serverAndClientsSites.getOpenShares().toArray()[0];

            share
                    .addListener(new TransactedSet.Listener<TransactedObject>() {

                        public void onAdded(Transaction transaction,
                                            TransactedObject object) {
//                            if (object instanceof ServerInfo)
//                                addInfo((ServerInfo) object);
//
//                            if (object instanceof TransactedList<?>)
//                                addList((TransactedList<triggerInfoObject>) object);

                        }

                        public void onRemoved(Transaction transaction,
                                              TransactedObject object) {
                        }
                    });


            for (TransactedObject o : share) {
//                if (o instanceof ServerInfo)
//                    addInfo((ServerInfo) o);
//
//                if (o instanceof TransactedList<?>)
//                    addList((TransactedList<triggerInfoObject>) o);

            }
            success = true;

        } catch (Transaction.AbortedException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        } catch (IOException e3) {
            // TODO Auto-generated catch block
            e3.printStackTrace();
        }


        return success;
    }


    private static void addList(final TransactedList<triggerInfoObject> info) {

        info.addListener(new FieldListener() {
            public void onChange(Transaction transaction, int i) {

            }
        });

    }


    public static void loadTriggers() {
        DispatcherUIController.appendTextToStatus("Shared Transacted list Added for Triggers..");
        if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
            Site.getLocal().allowThread();
            Transaction transaction = Site.getLocal().startTransaction();
            share.add(TriggerManager.triggers);
            transaction.commit();
        }
        TriggerManager.triggers.addListener(new FieldListener() {
            public void onChange(Transaction transaction, int i) {
                DispatcherUIController.appendTrigger(String.valueOf(TriggerManager.triggers.get(TriggerManager.triggers.size() - 1).gettrigger()));
            }
        });

    }

    public static void loadIPList() {
        DispatcherUIController.appendTextToStatus("Shared Transacted list Added for IPs..");
        if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
            Site.getLocal().allowThread();
            Transaction transaction = Site.getLocal().startTransaction();
            share.add(ClusterLeaderIpListManager.ipList);
            transaction.commit();
        }
        ClusterLeaderIpListManager.ipList.addListener(new FieldListener() {
            public void onChange(Transaction transaction, int i) {
                DispatcherUIController.appendIP("IP added to List: " + ClusterLeaderIpListManager.ipList.get(i));
            }
        });
    }
}
