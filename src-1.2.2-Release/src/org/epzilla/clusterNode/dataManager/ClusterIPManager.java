package org.epzilla.clusterNode.dataManager;

import jstm.core.Site;
import jstm.core.TransactedList;
import jstm.core.Transaction;
import org.epzilla.clusterNode.clusterInfoObjectModel.NodeIPObject;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: Mar 4, 2010
 * Time: 10:24:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClusterIPManager {
    private static TransactedList<NodeIPObject> ipList = new TransactedList<NodeIPObject>();
    static int count = 0;
    private static ArrayList<String> ipArr = new ArrayList<String>();

    public static void addIP(String clusterID, String ip) {
        if (getIpList() != null) {
            if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
                Site.getLocal().allowThread();
                Transaction transaction = Site.getLocal().startTransaction();
                NodeIPObject obj = new NodeIPObject();
                obj.setIP(ip);
                obj.setclusterID(clusterID);
                getIpList().add(obj);
                transaction.commit();
                count++;
            }
        }
    }

    public static void removeIP(String ip) {
        if (getIpList() != null) {
            if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
                Site.getLocal().allowThread();
                Transaction transaction = Site.getLocal().startTransaction();
                for (int i = 0; i < getIpList().size(); i++) {
                    if (getIpList().get(i).getIP().equals(ip)) {
                        getIpList().remove(i);
                        break;
                    }
                }
                transaction.commit();
                count++;
            }
        }
    }

    //add by chathura

    public static ArrayList<String> getNodeIpList() {
        if (getIpList() != null) {
            for (int i = 0; i < ipList.size(); i++) {
                if (!"IP".equalsIgnoreCase(ipList.get(i).getIP()))
                    ipArr.add(ipList.get(i).getIP());
            }
        }
        return ipArr;
    }

    public static TransactedList<NodeIPObject> getIpList() {
        return ipList;
    }

    public static void setIpList(TransactedList<NodeIPObject> ipList) {
        ClusterIPManager.ipList = ipList;
    }
}
