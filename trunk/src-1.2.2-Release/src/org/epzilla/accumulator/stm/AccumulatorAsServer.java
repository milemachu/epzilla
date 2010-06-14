package org.epzilla.accumulator.stm;

import jstm.core.*;
import jstm.transports.clientserver.Server;
import jstm.transports.clientserver.socket.SocketServer;


import java.io.IOException;

import org.epzilla.accumulator.generated.AccumulatorObjectModel;
import org.epzilla.accumulator.generated.LeaderInfoObject;
import org.epzilla.accumulator.dataManager.ClusterLeaderManager;
import org.epzilla.accumulator.userinterface.AccumulatorUIControler;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: May 18, 2010
 * Time: 1:28:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class AccumulatorAsServer {
    private static Share share;
    private static Server server;

    public static boolean startServer() {
        boolean success = false;
        AccumulatorUIControler.appendEventResults("Strating STM server...");
        Site.getLocal().registerObjectModel(new AccumulatorObjectModel());
        try {
            int port = 4444;
            server = new SocketServer(port);
            server.start();
            AccumulatorUIControler.appendEventResults("Attaching a share to sites group: server and clients...");

            share = new Share();
            AccumulatorUIControler.appendEventResults("Waiting For Clients...");
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

//            checkServerStatus();

        } catch (Transaction.AbortedException e2) {

            e2.printStackTrace();
        } catch (IOException e3) {

            e3.printStackTrace();
        }
        return success;
    }

    public static void loadIPList() {
        AccumulatorUIControler.appendEventResults("Shared Transacted list Added for IPs..");
        if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
            Site.getLocal().allowThread();
            Transaction transaction = Site.getLocal().startTransaction();
            LeaderInfoObject obj = new LeaderInfoObject();
            obj.setleaderIP("IP");
            ClusterLeaderManager.getDetailsList().add(obj);
            share.add(ClusterLeaderManager.getDetailsList());
            transaction.commit();
        }
        ClusterLeaderManager.getDetailsList().addListener(new FieldListener() {
            public void onChange(Transaction transaction, int i) {

            }
        });
    }

}
