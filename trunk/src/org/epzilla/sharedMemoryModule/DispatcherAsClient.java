package org.epzilla.sharedMemoryModule;

import jstm.core.*;
import jstm.transports.clientserver.socket.SocketClient;
import jstm.transports.clientserver.ConnectionInfo;

import java.util.Set;


import org.epzilla.dispatcher.NodeVariables;
import org.epzilla.dispatcher.TriggerManager;
import org.epzilla.dispatcher.ClusterLeaderIpListManager;
import org.epzilla.dispatcher.DispatcherUIController;
import generatedObjectModels.dispatcherObjectModel;
import generatedObjectModels.triggerInfoObject;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: Feb 18, 2010
 * Time: 3:58:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class DispatcherAsClient {
    private static Share share;
    private static SocketClient client;

    public static boolean startClient() {
        boolean success = false;
        DispatcherUIController.appendTextToStatus("Starting STM Client on: " + NodeVariables.getNodeIP());
        Site.getLocal().registerObjectModel(new dispatcherObjectModel());
        try {
            client = new SocketClient(NodeVariables.getCurrentServerIP(), NodeVariables.getPort());
            ConnectionInfo connection = client.connect();
            DispatcherUIController.appendTextToStatus("Client Started Successfully.... ");
            DispatcherUIController.appendTextToStatus("Connected to server: " + connection.getServer().getObjectModelUID().toString());
            share = new Share();

            DispatcherUIController.appendTextToStatus("Number of Open Shares: "
                    + String.valueOf(connection.getServerAndClients()
                    .getOpenShares().size()));
            // Once connected, retrieve the Group that represents the
            // server and its
            // clients

            Set<Share> shares = connection.getServerAndClients()
                    .getOpenShares();

            // Open a share in this group is there is none yet

            share = (Share) shares.toArray()[0];

            share
                    .addListener(new TransactedSet.Listener<TransactedObject>() {

                        public void onAdded(Transaction transaction,
                                            TransactedObject object) {

                            if (object instanceof TransactedList<?>)
                                addList((TransactedList<triggerInfoObject>) object);

                            if (object instanceof TransactedArray<?>)
                                addIpList((TransactedArray<String>) object);


                        }

                        public void onRemoved(Transaction transaction,
                                              TransactedObject object) {
                            if (object instanceof TransactedList<?>)
                                removeList((TransactedList<triggerInfoObject>) object);
                        }
                    });

            // The share might already contain Lists add them to local memory

            for (TransactedObject o : share) {

                if (o instanceof TransactedArray<?>)
                    addIpList((TransactedArray<String>) o);

                if (o instanceof TransactedList<?>) {
                    addList((TransactedList<triggerInfoObject>) o);
                }

            }
            success = true;
        } catch (Exception e2) {
            DispatcherUIController.appendTextToStatus("Attempt to Start Client Failed... ");
            DispatcherUIController.appendTextToStatus(e2.getMessage());
        }


        return success;
    }

    private static void addList(final TransactedList<triggerInfoObject> info) {

        DispatcherUIController.appendTextToStatus("TriggerList added.");
        TriggerManager.triggers = info;
        info.addListener(new FieldListener() {
            public void onChange(Transaction transaction, int i) {
                DispatcherUIController.appendTrigger(String.valueOf(TriggerManager.triggers.get(TriggerManager.triggers.size() - 1).gettrigger()));
            }
        });

    }

    private static void addIpList(final TransactedArray<String> info) {

        DispatcherUIController.appendTextToStatus("IPList added.");
        ClusterLeaderIpListManager.ipList = info;
        info.addListener(new FieldListener() {
            public void onChange(Transaction transaction, int i) {
                DispatcherUIController.appendIP("IP added to List: " + ClusterLeaderIpListManager.ipList.get(i));
            }
        });

    }


    private static void removeList(final TransactedList<triggerInfoObject> info) {


    }

}
