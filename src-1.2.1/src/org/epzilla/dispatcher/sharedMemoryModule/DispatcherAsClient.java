package org.epzilla.dispatcher.sharedMemoryModule;

import jstm.core.*;
import jstm.transports.clientserver.socket.SocketClient;
import jstm.transports.clientserver.ConnectionInfo;

import java.util.Set;
import java.util.TimerTask;


import org.epzilla.dispatcher.dataManager.ClientManager;
import org.epzilla.dispatcher.dataManager.ClusterLeaderIpListManager;
import org.epzilla.dispatcher.dataManager.NodeVariables;
import org.epzilla.dispatcher.dataManager.TriggerManager;
import org.epzilla.dispatcher.controlers.*;
import org.epzilla.dispatcher.dispatcherObjectModel.DispatcherObjectModel;
import org.epzilla.dispatcher.dispatcherObjectModel.TriggerInfoObject;
import org.epzilla.dispatcher.dispatcherObjectModel.ClientInfoObject;
import org.epzilla.dispatcher.dispatcherObjectModel.LeaderInfoObject;


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
        Site.getLocal().registerObjectModel(new DispatcherObjectModel());
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
                                addList((TransactedList<?>) object);

                        }

                        public void onRemoved(Transaction transaction,
                                              TransactedObject object) {
//                            if (object instanceof TransactedList<?>)
//                                removeList((TransactedList<TriggerInfoObject>) object);
                        }
                    });

            // The share might already contain Lists add them to local memory

            for (TransactedObject o : share) {

                if (o instanceof TransactedList<?>) {
                    addList((TransactedList<?>) o);
                }

            }
            success = true;
        } catch (Exception e2) {
            DispatcherUIController.appendTextToStatus("Attempt to Start Client Failed... ");
            DispatcherUIController.appendTextToStatus(e2.getMessage());
        }


        return success;
    }

    private static void addList(final TransactedList<?> info) {
        
        if (info.get(0) instanceof TriggerInfoObject) {
            addTriggerList((TransactedList<TriggerInfoObject>) info);
        }
        if (info.get(0) instanceof ClientInfoObject) {
           addClientList((TransactedList<ClientInfoObject>) info);
        }
        if (info.get(0) instanceof LeaderInfoObject) {
           addIpList((TransactedList<LeaderInfoObject>) info);
        }
    }

    private static void addTriggerList(final TransactedList<TriggerInfoObject> info) {
        DispatcherUIController.appendTextToStatus("TriggerList added.");
        TriggerManager.setTriggers(info);
        info.addListener(new FieldListener() {
            public void onChange(Transaction transaction, int i) {
                DispatcherUIController.appendTrigger(String.valueOf(TriggerManager.getTriggers().get(TriggerManager.getTriggers().size() - 1).gettrigger()));
            }
        });
    }

    private static void addClientList(final TransactedList<ClientInfoObject> info) {
        DispatcherUIController.appendTextToStatus("TriggerList added.");
        ClientManager.setClientList(info);
        info.addListener(new FieldListener() {
            public void onChange(Transaction transaction, int i) {
//                DispatcherUIController.appendTrigger(String.valueOf(TriggerManager.triggers.get(TriggerManager.triggers.size() - 1).gettrigger()));
            }
        });
    }

    private static void addIpList(final TransactedList<LeaderInfoObject> info) {

        DispatcherUIController.appendTextToStatus("IPList added.");
        ClusterLeaderIpListManager.setIpList(info);
        info.addListener(new FieldListener() {
            public void onChange(Transaction transaction, int i) {
                DispatcherUIController.appendIP("IP added to List: " + ClusterLeaderIpListManager.getIpList().get(i));
            }
        });

    }


    public static void checkServerStatus() {
        final java.util.Timer timer1 = new java.util.Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                if (client.getStatus() == SocketClient.Status.DISCONNECTED) {
                  DispatcherUIController.appendTextToStatus("Server Status..." + client.getStatus().toString());
                  this.cancel();
                }
            }
        }, 10, 1000);
    }

//    private static void removeList(final TransactedList<TriggerInfoObject> info) {
//
//    }


}
