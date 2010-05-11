package org.epzilla.dispatcher.clusterHandler;

import org.epzilla.dispatcher.dataManager.ClusterLeaderIpListManager;

/**
 * Created by IntelliJ IDEA.
 * User: Chathura
 * Date: May 10, 2010
 * Time: 9:58:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class LeaderDisconnector {
    public LeaderDisconnector() {
        
    }
    /*
    when a cluster leader is down, it needs to remove the Leader's IP fromn the STM
    purpose of this class is to remove failed leder IPs from the STM
     */
    public void leaderRemover(String leaderIP){
        ClusterLeaderIpListManager.removeIP(leaderIP);
    }
}
