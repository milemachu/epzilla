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
