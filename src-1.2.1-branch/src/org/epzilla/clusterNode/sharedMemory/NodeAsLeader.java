package org.epzilla.clusterNode.sharedMemory;

import jstm.core.*;
import jstm.transports.clientserver.Server;
import jstm.transports.clientserver.socket.SocketServer;
import org.epzilla.clusterNode.NodeController;
import org.epzilla.clusterNode.clusterInfoObjectModel.ClusterObjectModel;
import org.epzilla.clusterNode.clusterInfoObjectModel.NodeIPObject;
import org.epzilla.clusterNode.clusterInfoObjectModel.PerformanceInfoObject;
import org.epzilla.clusterNode.clusterInfoObjectModel.TriggerObject;
import org.epzilla.clusterNode.dataManager.ClusterIPManager;
import org.epzilla.clusterNode.dataManager.EventsManager;
import org.epzilla.clusterNode.dataManager.PerformanceInfoManager;
import org.epzilla.clusterNode.dataManager.TriggerManager;
import org.epzilla.clusterNode.leaderReg.ClusterStartup;
import org.epzilla.clusterNode.processor.EventProcessor;
import org.epzilla.clusterNode.userInterface.NodeUIController;
import org.epzilla.clusterNode.xml.ClusterSettingsReader;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.TimerTask;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: Mar 7, 2010
 * Time: 7:54:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class NodeAsLeader {
    private static Share share;
    private static Server server;
    static int sendingInterval;
    static int cpuUpperThreshHold;
    static int memUpperThreshHold;
    static int cpuLowerThreshHold;

    public static boolean startServer() {
        boolean success = false;
        NodeUIController.appendTextToStatus("Strating STM server...");
        Site.getLocal().registerObjectModel(new ClusterObjectModel());
        try {
            int port = NodeController.getPort();
            server = new SocketServer(port);
            server.start();
            NodeUIController.setLeaderStatus("Leader");
            NodeUIController.appendTextToStatus("Attaching a share to sites group: server and clients...");

            share = new Share();
            NodeUIController.appendTextToStatus("Waiting For Clients...");
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
            success = true;
        } catch (Transaction.AbortedException e2) {

            e2.printStackTrace();
        } catch (IOException e3) {

            e3.printStackTrace();
        }
        return success;
    }

    public static void loadTriggers() {
        NodeUIController.appendTextToStatus("Adding TransactedList for Triggers...");
        if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
            Site.getLocal().allowThread();
            Transaction transaction = Site.getLocal().startTransaction();
            TriggerObject obj = new TriggerObject();
            obj.settriggerID("OOOO");
            obj.settrigger("OOOO");
            TriggerManager.getTriggers().add(obj);
            share.add(TriggerManager.getTriggers());
            transaction.commit();
        }
        TriggerManager.getTriggers().addListener(new FieldListener() {
            public void onChange(Transaction transaction, int i) {
//                NodeUIController.appendTextToTriggerList(String.valueOf(TriggerManager.getTriggers().get(TriggerManager.getTriggers().size() - 1).gettrigger()));
                try {
                    TriggerObject trig = TriggerManager.getTriggers().get(TriggerManager.getTriggers().size() - 1);
                    if ("OOOO".equals(trig.gettrigger())) {
                        return;
                    }

                    NodeUIController.appendTextToTriggerList(trig.gettrigger());
                    EventProcessor.getInstance().addTrigger(trig.gettrigger(), trig.getclientID());
                } catch (Exception e) {
                    org.epzilla.util.Logger.error("stm err", e);
                }
            }
        });

    }

    public static void loadIPList() {
        NodeUIController.appendTextToStatus("Adding TransactedList for Node IPs...");
        if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
            Site.getLocal().allowThread();
            Transaction transaction = Site.getLocal().startTransaction();
            NodeIPObject obj = new NodeIPObject();
            InetAddress inetAddress;
            try {
                inetAddress = InetAddress.getLocalHost();
                String ipAddress = inetAddress.getHostAddress();
                obj.setIP(ipAddress);
                ClusterIPManager.getIpList().add(obj);
                share.add(ClusterIPManager.getIpList());
                transaction.commit();
            } catch (UnknownHostException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }
        ClusterIPManager.getIpList().addListener(new FieldListener() {
            public void onChange(Transaction transaction, int i) {
                EventsManager.loadNodesDetails();
            }
        });
    }

    public static void loadPerformanceInfoList() {
        NodeUIController.appendTextToStatus("Adding TransactedList for Load Balance Info...");
        if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
            Site.getLocal().allowThread();
            Transaction transaction = Site.getLocal().startTransaction();
            PerformanceInfoObject obj = new PerformanceInfoObject();
            obj.setnodeIP("PPPP");
            PerformanceInfoManager.getPerformanceList().add(obj);
            share.add(PerformanceInfoManager.getPerformanceList());
            transaction.commit();
        }
        PerformanceInfoManager.getPerformanceList().addListener(new FieldListener() {
            public void onChange(Transaction transaction, int i) {

                PerformanceInfoObject obj = PerformanceInfoManager.getPerformanceList().get(i);
                NodeUIController.appendTextToStatus("Load Balancing Info Recieved:: IP:" + obj.getnodeIP() + " CPU Usage:" + obj.getCPUusageAverage() + "% Memory Usage:" + obj.getMemUsageAverage() + "%");
            }
        });
    }


//    public static void checkServerStatus() {
//        final java.util.Timer timer1 = new java.util.Timer();
//        timer1.schedule(new TimerTask() {
//            @Override
//            public void run() {
////                Group set = server.getServerAndClients();
//
////                if (client.getStatus() == SocketClient.Status.DISCONNECTED) {
//////                     DispatcherUIController.appendAccumulatorStatus("Server Status..." + client.getStatus().toString());
////                    this.cancel();
////                }
//            }
//        }, 10, 1000);
//    }

    //Check if the cluster is over loaded=> needs new nodes.

    public static void checkForOverloading() {
        final java.util.Timer timer1 = new java.util.Timer();
        ArrayList<String[]> settingsList = ClusterSettingsReader.getServerIPSettings("clusterID_settings.xml");
        String[] settings = settingsList.get(0);
        if (settings != null) {
            sendingInterval = Integer.valueOf(settings[0]);
            cpuUpperThreshHold = Integer.valueOf(settings[1]);
            memUpperThreshHold = Integer.valueOf(settings[2]);
            cpuLowerThreshHold = Integer.valueOf(settings[3]);
        } else {
            sendingInterval = 20000;
            cpuUpperThreshHold = 90;
            memUpperThreshHold = 90;
            cpuLowerThreshHold = 5;
        }
        timer1.schedule(new TimerTask() {
            Hashtable<String, Integer> cpuArray = new Hashtable<String, Integer>();
            ArrayList<Integer> memArray = new ArrayList<Integer>();

            @Override
            public void run() {
                TransactedList<PerformanceInfoObject> list = PerformanceInfoManager.getPerformanceList();
                cpuArray.clear();
                memArray.clear();
                int CPUsum = 0;
                int MemSum = 0;
                for (int i = list.size() - 1; i >= 0; i--) {
                    if (list.get(i).getnodeIP() != "PPPP") {
                        if (!cpuArray.containsKey(list.get(i).getnodeIP())) {
                            cpuArray.put(list.get(i).getnodeIP(), Integer.valueOf(list.get(i).getCPUusageAverage()));
                            memArray.add(Integer.valueOf(list.get(i).getMemUsageAverage()));
                        }
                    }
                }
                Enumeration keys = cpuArray.keys();
                while (keys.hasMoreElements()) {
                    CPUsum += cpuArray.get(keys.nextElement());
                }

                for (int i = 0; i < memArray.size(); i++) {
                    MemSum += memArray.get(i);
                }

                if (cpuArray.size() > 0) {
                    int cpuResult = (int) (CPUsum / cpuArray.size());
                    int memResult = (int) (MemSum / cpuArray.size());
                    NodeUIController.appendTextToStatus("Average CPU usage of the cluster: " + cpuResult + "%");
                    NodeUIController.appendTextToStatus("Average Memory usage of the cluster: " + memResult + "%");

                    if (cpuResult >= cpuUpperThreshHold && memResult >= memUpperThreshHold) {
                        // Add node

                    } else if (cpuResult < cpuLowerThreshHold) {
                        //Remove Node


                    }

                    //send perfomance info
                    try {
                        ClusterStartup.sendInfo(cpuResult, memResult);
                        NodeUIController.appendTextToStatus("Performance Info Sent to Dispatcher...");
                    } catch (Exception e) {
                        NodeUIController.appendTextToStatus("Cannot Reach Dispatcher");
                    }
                }
            }
        }, sendingInterval, sendingInterval);
    }


}


