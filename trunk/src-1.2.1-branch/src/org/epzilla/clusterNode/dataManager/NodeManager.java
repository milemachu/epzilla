package org.epzilla.clusterNode.dataManager;

import jstm.core.Site;
import jstm.core.TransactedList;
import jstm.core.Transaction;
import org.epzilla.clusterNode.clusterInfoObjectModel.NodeStatusObject;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: Jun 22, 2010
 * Time: 2:38:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class NodeManager {
    private static TransactedList<NodeStatusObject> inactiveipList = new TransactedList<NodeStatusObject>();

    public static void addIP(String clusterID, String ip) {
        if (getInactiveipList() != null) {
            if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
                Site.getLocal().allowThread();
                Transaction transaction = Site.getLocal().startTransaction();
                NodeStatusObject obj = new NodeStatusObject();
                obj.setIP(ip);
                obj.setclusterID(clusterID);
                getInactiveipList().add(obj);
                transaction.commit();
            }
        }
    }

    public static String removeIP() {
        String ip = null;
        if (getInactiveipList() != null) {
            if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
                Site.getLocal().allowThread();
                Transaction transaction = Site.getLocal().startTransaction();
                for (int i = 1; i < getInactiveipList().size(); i++) {
                    ip = getInactiveipList().get(i).getIP();
                    getInactiveipList().remove(i);
                }
                transaction.commit();
            }
        }
        return ip;
    }


    public static TransactedList<NodeStatusObject> getInactiveipList() {
        return inactiveipList;
    }

    public static void setInactiveipList(TransactedList<NodeStatusObject> inactiveipList) {
        NodeManager.inactiveipList = inactiveipList;
    }
}
