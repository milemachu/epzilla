package org.epzilla.dispatcher.sharedMemoryModule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.TimerTask;

import net.epzilla.stratification.dynamic.DynamicDependencyManager;
import org.epzilla.dispatcher.controlers.*;
import org.epzilla.dispatcher.dataManager.*;
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
                //DispatcherUIController.appendTrigger(String.valueOf(TriggerManager.getTriggers().get(TriggerManager.getTriggers().size() - 1).gettrigger()));
            TriggerManager.printTriggers();
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

    public static void loadPerformanceInfoList() {
        DispatcherUIController.appendTextToStatus("Shared Transacted List Added for Load Balance Info...");
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
                DispatcherUIController.appendTextToStatus("Dispatcher Performance:: IP:" + obj.getnodeIP() + " CPU Usage:" + obj.getCPUusageAverage() + "% Memory Usage:" + obj.getMemUsageAverage() + "%");
            }
        });
    }


     public static void checkForOverloading() {
        final java.util.Timer timer1 = new java.util.Timer();
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
                for (int i = list.size()-1; i >=0 ; i--) {
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
                     DispatcherUIController.appendTextToStatus("Average CPU usage of Dispatchers: " + cpuResult + "%");
                     DispatcherUIController.appendTextToStatus("Average Memory usage of Dispatchers: " + memResult + "%");

                    //TO DO add new node

                }
            }
        }, 120000, 200000);
    }

}
