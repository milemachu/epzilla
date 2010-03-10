package org.epzilla.clusterNode.sharedMemory;

import jstm.core.*;
import jstm.transports.clientserver.Server;
import jstm.transports.clientserver.socket.SocketServer;
import org.epzilla.clusterNode.clusterInfoObjectModel.ClusterObjectModel;
import org.epzilla.clusterNode.clusterInfoObjectModel.TriggerObject;
import org.epzilla.clusterNode.clusterInfoObjectModel.NodeIPObject;
import org.epzilla.clusterNode.NodeController;
import org.epzilla.clusterNode.dataManager.TriggerManager;
import org.epzilla.clusterNode.dataManager.ClusterIPManager;


import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: Mar 7, 2010
 * Time: 7:54:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class NodeAsLeader {
    private static Share share;

    public static boolean startServer() {
        boolean success = false;
        System.out.println("Strating STM server");
        Site.getLocal().registerObjectModel(new ClusterObjectModel());
        try {
            int port = NodeController.getPort();
            Server server = new SocketServer(port);
            server.start();
            System.out.println("Attaching a share to sites group: server and clients...");

            share = new Share();
            System.out.println("Waiting For Clients...");
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
            System.out.println("Server Successfully started...");
            System.out.println("Waiting for Clients...");
            System.in.read();

        } catch (Transaction.AbortedException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        } catch (IOException e3) {
            // TODO Auto-generated catch block
            e3.printStackTrace();
        }
        return success;
    }

    public static void loadTriggers() {

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
                System.out.println(String.valueOf(TriggerManager.getTriggers().get(TriggerManager.getTriggers().size() - 1).gettrigger()));
            }
        });

    }

    public static void loadIPList() {

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
                System.out.println("New IP added to IP List");

            }
        });
    }

}


