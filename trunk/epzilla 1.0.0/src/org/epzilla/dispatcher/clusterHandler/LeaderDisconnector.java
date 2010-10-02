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
package org.epzilla.dispatcher.clusterHandler;

import org.epzilla.dispatcher.dataManager.ClusterLeaderIpListManager;

/**
 * Created by IntelliJ IDEA.
 * This class is to remove the Cluster leader from the Dispatcher's cluster list
 * Author: Chathura
 * Date: May 10, 2010
 * Time: 9:58:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class LeaderDisconnector {
    public LeaderDisconnector() {

    }

    /**
     * when a cluster leader is down, it needs to remove the Leader's IP fromn the STM
     * purpose of this class is to remove failed leder IPs from the STM
     *
     * @param leaderIP
     */
    public void leaderRemover(String leaderIP) {
        ClusterLeaderIpListManager.removeIP(leaderIP);
    }
}
