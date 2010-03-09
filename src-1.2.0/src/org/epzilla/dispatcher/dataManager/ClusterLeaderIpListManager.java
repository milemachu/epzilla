package org.epzilla.dispatcher.dataManager;

import jstm.core.TransactedList;
import jstm.core.Site;
import jstm.core.Transaction;

import java.util.TimerTask;

import org.epzilla.dispatcher.dispatcherObjectModel.LeaderInfoObject;
import org.epzilla.dispatcher.DispatcherUIController;


/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: Feb 24, 2010
 * Time: 9:23:09 AM
 * To change this template use File | Settings | File Templates.
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
                count++;
            }
        }
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
                    count++;
                }
            } catch (Exception e) {

            }
        }
        printIPList();
    }

    public static void printIPList() {
        DispatcherUIController.clearIPList();
        int size = getIpList().size();
        LeaderInfoObject[] arr = new LeaderInfoObject[size + 2];
        arr = getIpList().toArray(arr);
        for (int i = 1; i < size; i++) {
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
