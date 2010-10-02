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
package org.epzilla.dispatcher.dataManager;

import jstm.core.Site;
import jstm.core.TransactedList;
import jstm.core.Transaction;
import net.epzilla.stratification.dynamic.SystemVariables;
import org.epzilla.dispatcher.controlers.DispatcherUIController;
import org.epzilla.dispatcher.dispatcherObjectModel.LeaderInfoObject;

import java.util.ArrayList;
import java.util.TimerTask;


/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: Feb 24, 2010
 * Time: 9:23:09 AM
 * To change this template use File | Settings | File Templates.
 */

/**
 * Manage The Cluster Leader IP List
 */
public class ClusterLeaderIpListManager {
    private static TransactedList<LeaderInfoObject> ipList = new TransactedList<LeaderInfoObject>(20);
    static int count = 0;

    // Code For Testing Only -Dishan
    public static void loadSampleIPs() {
        final java.util.Timer timer1 = new java.util.Timer();
        timer1.schedule(new TimerTask() {

            @Override
            public void run() {
                if (getIpList() != null) {
                    if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
                        Site.getLocal().allowThread();
                        Transaction transaction = Site.getLocal().startTransaction();
                        String ip = "192.168.1." + String.valueOf(count);
                        LeaderInfoObject obj = new LeaderInfoObject();
                        obj.setleaderIP(ip);
                        getIpList().add(obj);
                        transaction.commit();
                    }
                    count++;

                    if (count == 10)
                        timer1.cancel();
                }
            }
        }, 10, 50);
        removeIP("192.168.1.1");
        removeIP("192.168.1.2");

    }

    public static void addIP(String clusterID, String ip) {
        if (getIpList() != null) {
            if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
                Site.getLocal().allowThread();
                Transaction transaction = Site.getLocal().startTransaction();
                LeaderInfoObject obj = new LeaderInfoObject();
                obj.setclusterID(clusterID);
                obj.setleaderIP(ip);
                getIpList().add(obj);
                transaction.commit();
            }
        }
        printIPList();//print cluster leader Ip list periodically
        int size = getIpList().size();
        SystemVariables.setClusters(0, size - 1);
    }

    public static void removeIP(String ip) {
        if (getIpList() != null) {
            try {
                if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
                    Site.getLocal().allowThread();
                    Transaction transaction = Site.getLocal().startTransaction();
                    for (int i = 0; i < ipList.size(); i++) {
                        if (ipList.get(i).getleaderIP().equals(ip)) {
                            ipList.remove(i);
                            break;
                        }
                    }
                    transaction.commit();
                }
            } catch (Exception e) {

            }
        }
        printIPList();
    }

    public static void clearIPList() {
        if (getIpList() != null) {
            try {
                if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
                    Site.getLocal().allowThread();
                    Transaction transaction = Site.getLocal().startTransaction();
                    ipList.clear();
                    transaction.commit();
                }
            } catch (Exception e) {

            }
        }
        printIPList();
    }


    //add by chathura to get cluster leader ip list

    public static ArrayList<String> getClusterIpList() {
        ArrayList<String> ipArr = new ArrayList<String>();
        if (getIpList() != null) {
            for (int i = 0; i < ipList.size(); i++) {
                String ip = ipList.get(i).getleaderIP();
                if (!"IP".equalsIgnoreCase(ip)) {
                    ipArr.add(ip);
                }
            }
        }
        return ipArr;
    }
    //add by chathura to get cluster id list

    public static ArrayList<String> getClusterIdList() {
        ArrayList<String> idArr = new ArrayList<String>();
        if (getIpList() != null) {
            for (int i = 0; i < ipList.size(); i++) {
                LeaderInfoObject lp = ipList.get(i);
                if (!"IP".equalsIgnoreCase(lp.getleaderIP())) {
                    idArr.add(lp.getclusterID());
                }
            }
        }
        return idArr;
    }

    public static void printIPList() {
        DispatcherUIController.clearIPList();
        int size = getIpList().size();
        LeaderInfoObject[] arr = new LeaderInfoObject[size + 2];
        arr = getIpList().toArray(arr);
        for (int i = 0; i < size; i++) {
            DispatcherUIController.appendIP(arr[i].getleaderIP());
        }
    }

    public static TransactedList<LeaderInfoObject> getIpList() {
        return ipList;
    }

    public static void setIpList(TransactedList<LeaderInfoObject> ipList) {
        ClusterLeaderIpListManager.ipList = ipList;
    }
}
