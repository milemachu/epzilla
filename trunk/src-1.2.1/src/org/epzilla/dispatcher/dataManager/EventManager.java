package org.epzilla.dispatcher.dataManager;

import org.epzilla.dispatcher.dispatcherObjectModel.LeaderInfoObject;

import java.util.ArrayList;

import jstm.core.TransactedList;
import org.epzilla.dispatcher.clusterHandler.*;

/**
 * Created by IntelliJ IDEA.
 * User: chathura
 * Date: May 2, 2010
 * Time: 4:24:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventManager {
    private static TransactedList<LeaderInfoObject> clusterLeaderIPs;
    private static TransactedList<String> clusterLeaderIds;
    private static String[] clusterLeader = new String[10];

    public static void sendEventsToClusters(ArrayList<String> eList) {
//            EventSender.acceptEventStream();
    }

    private static void loadClusterIPs() {
        clusterLeaderIPs = ClusterLeaderIpListManager.getIpList();
//        clusterLeaderIds =
    }
}
