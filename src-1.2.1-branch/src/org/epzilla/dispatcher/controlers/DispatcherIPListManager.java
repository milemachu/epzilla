package org.epzilla.dispatcher.controlers;

import org.epzilla.dispatcher.dataManager.ClusterLeaderIpListManager;
import org.epzilla.leader.LeaderElectionInitiator;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.TimerTask;

/**
 * Created by IntelliJ IDEA.
 * Class manages the Dispatcher IP lists
 * Author: Chathura
 * Date: May 31, 2010
 * Time: 3:33:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class DispatcherIPListManager {
    private static int UPDATE_SERVICE_RUNNING_TIME = 30000;
    private static int INITIAL_START_TIME = 5000;

    public static void Initialize() {
        addDispatcherIps();
        addClusterIps();
    }
    /*
    get the Dispatcher ips from the leader service,
    added them to the user interface of dispatcher
     */
    public static void addDispatcherIps() {
        final java.util.Timer timer1 = new java.util.Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                DispatcherUIController.clearDispatcherIpList();
                HashSet<String> ipList = LeaderElectionInitiator.getDispatchers();

                if (ipList != null) {
                    for (Object dispList : ipList) {
                        String ip = (String) dispList;
                        DispatcherUIController.appendDispatcherIPs(ip);
                    }
                    System.gc();
                }
            }
        }, INITIAL_START_TIME, UPDATE_SERVICE_RUNNING_TIME);
    }
    /*
    get cluster ips from the leader service
    add cluster ips to the Dispatcher user interface
     */
    public static void addClusterIps() {
        final java.util.Timer timer1 = new java.util.Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                Hashtable<Integer, String> leaders = LeaderElectionInitiator.getSubscribedClusterLeadersFromAnyDispatcher();
                if (leaders != null) {
                    ClusterLeaderIpListManager.clearIPList();
                    for (int key : leaders.keySet()) {
                        ClusterLeaderIpListManager.addIP("" + key, leaders.get(key));
                        System.out.println("leader " + leaders.get(key));
                    }
                }

                System.gc();

            }
        }, INITIAL_START_TIME, UPDATE_SERVICE_RUNNING_TIME);
    }
}
