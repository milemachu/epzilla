/***************************************************************************************************
Copyright 2009 - 2010 Harshana Eranga Martin, Dishan Metihakwala, Rajeev Sampath, Chathura Randika

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
****************************************************************************************************/
package org.epzilla.clusterNode.sharedMemory;

import jstm.core.*;
import jstm.transports.clientserver.socket.SocketClient;
import jstm.transports.clientserver.ConnectionInfo;


import java.util.Set;
import java.util.TimerTask;

import org.epzilla.clusterNode.clusterInfoObjectModel.*;
import org.epzilla.clusterNode.NodeController;
import org.epzilla.clusterNode.dataManager.NodeManager;
import org.epzilla.clusterNode.leaderReg.ClusterStartup;
import org.epzilla.clusterNode.processor.EventProcessor;
import org.epzilla.clusterNode.userInterface.NodeUIController;
import org.epzilla.clusterNode.dataManager.TriggerManager;
import org.epzilla.clusterNode.dataManager.ClusterIPManager;
import org.epzilla.clusterNode.dataManager.PerformanceInfoManager;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: Mar 7, 2010
 * Time: 7:55:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class NodeAsNonLeader {
    private static Share share;
    private static SocketClient client;
    private static boolean isActive;

    public static boolean startClient() {
        boolean success = false;
        NodeUIController.appendTextToStatus("Starting STM Client....");
        Site.getLocal().registerObjectModel(new ClusterObjectModel());
        try {
            client = new SocketClient(NodeController.getLeaderIP(), NodeController.getPort());
            ConnectionInfo connection = client.connect();
            NodeUIController.appendTextToStatus("Client Started Successfully.... ");
            NodeUIController.appendTextToStatus("Connected to server: " + NodeController.getLeaderIP().toString());
            isActive = true;
            share = new Share();

            NodeUIController.appendTextToStatus("Number of Open Shares: "
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

                        }
                    });

            // The share might already contain Lists add them to local memory

            for (TransactedObject o : share) {

                if (o instanceof TransactedList<?>) {
                    addList((TransactedList<?>) o);
                }

            }
            success = true;
            NodeUIController.setLeaderStatus("Non Leader");
            checkServerStatus();
        } catch (Exception e2) {
            NodeUIController.appendTextToStatus("Attempt to Start Client Failed... ");
            NodeUIController.appendTextToStatus(e2.getMessage());
        }


        return success;
    }

    private static void addList(final TransactedList<?> info) {

        if (info.get(0) instanceof TriggerObject) {
            addTriggerList((TransactedList<TriggerObject>) info);
        }
        if (info.get(0) instanceof NodeIPObject) {
            addIpList((TransactedList<NodeIPObject>) info);
        }
        if (info.get(0) instanceof PerformanceInfoObject) {
            addPerformanceInfoList((TransactedList<PerformanceInfoObject>) info);
        }
        if (info.get(0) instanceof NodeStatusObject) {
            addNodeStatusList((TransactedList<NodeStatusObject>) info);
        }

    }

    private static void addTriggerList(final TransactedList<TriggerObject> info) {
        NodeUIController.appendTextToStatus("TriggerList added.");
        TriggerManager.setTriggers(info);
        info.addListener(new FieldListener() {
            public void onChange(Transaction transaction, int i) {
                try {
                    if (isActive) {
                        TriggerObject trig = TriggerManager.getTriggers().get(TriggerManager.getTriggers().size() - 1);
                        if ("OOOO".equals(trig.gettrigger())) {
                            return;
                        }

                        NodeUIController.appendTextToTriggerList(trig.gettrigger());
                        EventProcessor.getInstance().addTrigger(trig.gettrigger(), trig.getclientID());
                    } else {
                        info.removeListener(this);
                    }
                } catch (Exception e) {
//                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    org.epzilla.util.Logger.error("stm err:", e);
                }


            }
        });


    }


    private static void addIpList(final TransactedList<NodeIPObject> info) {

        NodeUIController.appendTextToStatus("IPList added...");
        ClusterIPManager.setIpList(info);
        info.addListener(new FieldListener() {
            public void onChange(Transaction transaction, int i) {
                if (isActive) {
//                   DispatcherUIController.appendIP("IP added to List: " + ClusterLeaderIpListManager.getIpList().get(i));
                } else {
                    info.removeListener(this);
                }
            }
        });

    }

    private static void addNodeStatusList(final TransactedList<NodeStatusObject> info) {
        NodeManager.setInactiveipList(info);
    }

    private static void addPerformanceInfoList(final TransactedList<PerformanceInfoObject> info) {

        NodeUIController.appendTextToStatus("Performance Info List added...");
        PerformanceInfoManager.setPerformanceList(info);
        info.addListener(new FieldListener() {
            public void onChange(Transaction transaction, int i) {
                if (isActive) {
                    PerformanceInfoObject obj = PerformanceInfoManager.getPerformanceList().get(i);
                    NodeUIController.appendTextToStatus("Load Balancing Info Recieved:: IP:" + obj.getnodeIP() + " CPU Usage:" + obj.getCPUusageAverage() + "% Memory Usage:" + obj.getMemUsageAverage() + "%");

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
                final SocketClient.Status serverStatus = client.getStatus();
                if (serverStatus == SocketClient.Status.DISCONNECTED) {
                    NodeUIController.appendTextToStatus("Server Status :" + serverStatus);
                    this.cancel();
                    isActive = false;
                    //Initializing LE
                    ClusterStartup.triggerLEFromRemote();
                    ClusterStartup.startSTM();
                }
            }
        }, 10, 1000);
    }


}
