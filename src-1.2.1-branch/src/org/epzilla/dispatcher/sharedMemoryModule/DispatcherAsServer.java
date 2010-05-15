package org.epzilla.dispatcher.sharedMemoryModule;

import java.io.IOException;

import net.epzilla.stratification.immediate.DynamicDependencyManager;
import org.epzilla.dispatcher.*;
import org.epzilla.dispatcher.controlers.*;
import org.epzilla.dispatcher.dataManager.ClientManager;
import org.epzilla.dispatcher.dataManager.ClusterLeaderIpListManager;
import org.epzilla.dispatcher.dataManager.TriggerManager;
import org.epzilla.dispatcher.dataManager.NodeVariables;
import org.epzilla.dispatcher.dispatcherObjectModel.*;
import jstm.core.*;
import jstm.transports.clientserver.*;
import jstm.transports.clientserver.socket.SocketServer;


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
        Site.getLocal().registerObjectModel(new DispatcherObjectModel());
        try {
            int port = NodeVariables.getPort();
            Server server = new SocketServer(port);
            server.start();
            DispatcherUIController.appendTextToStatus("Attaching a share to sites group: server and clients...");

            share = new Share();
            Share metadataShare = new Share();

            DispatcherUIController.appendTextToStatus("Waiting For Clients...");
            // Once connected, retrieve the Group that represents the
            // server and its
            // clients

            Group serverAndClientsSites = server.getServerAndClients();
            ShareMarker sm = new ShareMarker();
            sm.setid("content");
            share.add(sm);

            sm = new ShareMarker();
            sm.setid("meta");
            metadataShare.add(sm);

            // Open a share in this group is there is none yet

            if (serverAndClientsSites.getOpenShares().size() == 0) {
                Transaction transaction = Site.getLocal().startTransaction();
                server.getServerAndClients().getOpenShares().add(share);
                server.getServerAndClients().getOpenShares().add(metadataShare);
                transaction.commit();
            }

            DynamicDependencyManager.setDependencyShare(metadataShare);

            // todo - embed stratification stuff...
//            share = (Share) serverAndClientsSites.getOpenShares().toArray()[0];
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


    public static void loadTriggers() {
        DispatcherUIController.appendTextToStatus("Shared Transacted list Added for Triggers..");
        if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
            Site.getLocal().allowThread();
            Transaction transaction = Site.getLocal().startTransaction();
            TriggerInfoObject obj = new TriggerInfoObject();
            obj.settriggerID("OOOO");
            obj.settrigger("OOOO");
            TriggerManager.getTriggers().add(obj);
            share.add(TriggerManager.getTriggers());
            transaction.commit();
        }
        TriggerManager.getTriggers().addListener(new FieldListener() {
            public void onChange(Transaction transaction, int i) {
                DispatcherUIController.appendTrigger(String.valueOf(TriggerManager.getTriggers().get(TriggerManager.getTriggers().size() - 1).gettrigger()));
            }
        });

    }

    public static void loadClientList() {
        DispatcherUIController.appendTextToStatus("Shared Transacted Map Added for Clients..");
        if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
            Site.getLocal().allowThread();
            Transaction transaction = Site.getLocal().startTransaction();
            share.add(ClientManager.getClientMap());
            transaction.commit();
        }
//        ClientManager.getClientMap().addListener(new );

    }


    public static void loadIPList() {
        DispatcherUIController.appendTextToStatus("Shared Transacted list Added for IPs..");
        if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
            Site.getLocal().allowThread();
            Transaction transaction = Site.getLocal().startTransaction();
            LeaderInfoObject obj = new LeaderInfoObject();
            obj.setleaderIP("IP");
            ClusterLeaderIpListManager.getIpList().add(obj);
            share.add(ClusterLeaderIpListManager.getIpList());
            transaction.commit();
        }
        ClusterLeaderIpListManager.getIpList().addListener(new FieldListener() {
            public void onChange(Transaction transaction, int i) {
//              DispatcherUIController.appendIP("IP added to List: " + ClusterLeaderIpListManager.getIpList().get(i));
                ClusterLeaderIpListManager.printIPList();
            }
        });
    }
}
