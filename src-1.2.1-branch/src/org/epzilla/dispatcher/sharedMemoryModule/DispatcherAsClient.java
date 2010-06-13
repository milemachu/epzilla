package org.epzilla.dispatcher.sharedMemoryModule;

import jstm.core.*;
import jstm.transports.clientserver.socket.SocketClient;
import jstm.transports.clientserver.ConnectionInfo;

import java.util.Set;
import java.util.TimerTask;


import net.epzilla.stratification.dynamic.DynamicDependencyManager;
import org.epzilla.dispatcher.dataManager.*;
import org.epzilla.dispatcher.controlers.*;
import org.epzilla.dispatcher.dispatcherObjectModel.*;


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
    private static boolean isActive;

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
            isActive = true;

            DispatcherUIController.appendTextToStatus("Number of Open Shares: "
                    + String.valueOf(connection.getServerAndClients()
                    .getOpenShares().size()));
            // Once connected, retrieve the Group that represents the
            // server and its
            // clients

            Set<Share> shares = connection.getServerAndClients()
                    .getOpenShares();

            // Open a share in this group is there is none yet

            Object[] ar = shares.toArray();
            share = (Share) ar[0];
            Share metaShare = (Share) ar[1];

            for (TransactedObject ob : share) {
                if (ob instanceof ShareMarker) {
                    if ("meta".equals(((ShareMarker) ob).getid())) {
                        // shares need to be swapped.
                        Share x = share;
                        share = metaShare;
                        metaShare = x;
                    }
                }
            }

            DynamicDependencyManager.setDependencyShare(metaShare);

            share
                    .addListener(new TransactedSet.Listener<TransactedObject>() {

                        public void onAdded(Transaction transaction,
                                            TransactedObject object) {

                            if (object instanceof TransactedList<?>)
                                addList((TransactedList<?>) object);

                            if (object instanceof TransactedMap<?, ?>)
                                addClientList((TransactedMap<?, ?>) object);

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
        if (info.get(0) instanceof LeaderInfoObject) {
            addIpList((TransactedList<LeaderInfoObject>) info);
        }
        if (info.get(0) instanceof PerformanceInfoObject) {
            addPerformanceInfoList((TransactedList<PerformanceInfoObject>) info);
        }
    }


    private static void addTriggerList(final TransactedList<TriggerInfoObject> info) {
        DispatcherUIController.appendTextToStatus("TriggerList added.");
        TriggerManager.setTriggers(info);
        info.addListener(new FieldListener() {
            public void onChange(Transaction transaction, int i) {

                if (isActive) {
                    DispatcherUIController.appendTrigger(String.valueOf(TriggerManager.getTriggers().get(TriggerManager.getTriggers().size() - 1).gettrigger()));
                } else {
                    info.removeListener(this);
                }
            }
        });
    }

    private static void addClientList(final TransactedMap info) {
        DispatcherUIController.appendTextToStatus("Client Map added.");
        ClientManager.setClientMap(info);
//        info.addListener(new FieldListener() {
//            public void onChange(Transaction transaction, int i) {
////                DispatcherUIController.appendTrigger(String.valueOf(TriggerManager.triggers.get(TriggerManager.triggers.size() - 1).gettrigger()));
//            }
//        });
    }

    private static void addIpList(final TransactedList<LeaderInfoObject> info) {

        DispatcherUIController.appendTextToStatus("IPList added.");
        ClusterLeaderIpListManager.setIpList(info);
        info.addListener(new FieldListener() {
            public void onChange(Transaction transaction, int i) {
                if (isActive) {
                    ClusterLeaderIpListManager.printIPList();
                } else {
                    info.removeListener(this);
                }
            }
        });

    }


    private static void addPerformanceInfoList(final TransactedList<PerformanceInfoObject> info) {

        DispatcherUIController.appendTextToStatus("Performance Info List added...");
        PerformanceInfoManager.setPerformanceList(info);
        info.addListener(new FieldListener() {
            public void onChange(Transaction transaction, int i) {
                if (isActive) {
                    PerformanceInfoObject obj = PerformanceInfoManager.getPerformanceList().get(i);
                    DispatcherUIController.appendTextToStatus("Load Balancing Info Recieved:: IP:" + obj.getnodeIP() + " CPU Usage:" + obj.getCPUusageAverage() + "% Memory Usage:" + obj.getMemUsageAverage() + "%");

                } else {
                    info.removeListener(this);
                }
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
                    isActive=false;
                }
            }
        }, 10, 1000);
    }


}
