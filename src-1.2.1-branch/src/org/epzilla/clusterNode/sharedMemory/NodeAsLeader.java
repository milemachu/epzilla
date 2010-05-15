package org.epzilla.clusterNode.sharedMemory;

import jstm.core.*;
import jstm.transports.clientserver.Server;
import jstm.transports.clientserver.socket.SocketServer;
import jstm.transports.clientserver.socket.SocketClient;
import org.epzilla.clusterNode.clusterInfoObjectModel.ClusterObjectModel;
import org.epzilla.clusterNode.clusterInfoObjectModel.TriggerObject;
import org.epzilla.clusterNode.clusterInfoObjectModel.NodeIPObject;
import org.epzilla.clusterNode.clusterInfoObjectModel.PerformanceInfoObject;
import org.epzilla.clusterNode.NodeController;
import org.epzilla.clusterNode.userInterface.NodeUIController;
import org.epzilla.clusterNode.dataManager.TriggerManager;
import org.epzilla.clusterNode.dataManager.ClusterIPManager;
import org.epzilla.clusterNode.dataManager.PerformanceInfoManager;


import java.io.IOException;
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

            checkServerStatus();

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
                NodeUIController.appendTextToTriggerList(String.valueOf(TriggerManager.getTriggers().get(TriggerManager.getTriggers().size() - 1).gettrigger()));
            }
        });

    }

    public static void loadIPList() {
       NodeUIController.appendTextToStatus("Adding TransactedList for Node IPs...");
        if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
            Site.getLocal().allowThread();
            Transaction transaction = Site.getLocal().startTransaction();
            NodeIPObject obj = new NodeIPObject();
            obj.setIP("IP");
            ClusterIPManager.getIpList().add(obj);
            share.add(ClusterIPManager.getIpList());
            transaction.commit();
        }
        ClusterIPManager.getIpList().addListener(new FieldListener() {
            public void onChange(Transaction transaction, int i) {


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
              NodeUIController.appendTextToStatus("Load Balancing Info Recieved:: IP:"+ obj.getnodeIP()+" CPU Usage:"+obj.getCPUusageAverage()+ "% Memory Usage:"+obj.getMemUsageAverage()+"%");
            }
        });
    }


    public static void checkServerStatus() {
        final java.util.Timer timer1 = new java.util.Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                Group set = server.getServerAndClients();

//                if (client.getStatus() == SocketClient.Status.DISCONNECTED) {
////                     DispatcherUIController.appendTextToStatus("Server Status..." + client.getStatus().toString());
//                    this.cancel();
//                }
            }
        }, 10, 1000);
    }

}


