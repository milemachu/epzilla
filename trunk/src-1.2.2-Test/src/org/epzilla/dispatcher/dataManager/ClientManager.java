package org.epzilla.dispatcher.dataManager;

import jstm.core.Site;
import jstm.core.Transaction;
import jstm.core.TransactedMap;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: Mar 4, 2010
 * Time: 10:33:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClientManager {
    private static TransactedMap clientMap = new TransactedMap();

    public static void addClient(String clientID, String ip) {
        if (getClientMap() != null) {
            if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
                Site.getLocal().allowThread();
                Transaction transaction = Site.getLocal().startTransaction();
                getClientMap().put(clientID, ip);
                transaction.commit();
            }
        }
    }

    public static void removeClient(String clientID) {
        if (getClientMap() != null) {
            if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
                Site.getLocal().allowThread();
                Transaction transaction = Site.getLocal().startTransaction();
                getClientMap().remove(clientID);
                transaction.commit();
            }
        }
    }

    public static TransactedMap getClientMap() {
        return clientMap;
    }

    public static void setClientMap(TransactedMap clientMap) {
        ClientManager.clientMap = clientMap;
    }

    public static String getClientIp(String clientID) {
        String cIP = "";
        if ((getClientMap() != null) && (getClientMap().containsKey(clientID)) ) {
            if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
                Site.getLocal().allowThread();
                Transaction transaction = Site.getLocal().startTransaction();
                cIP = (String) getClientMap().get(clientID);
                transaction.commit();
            }
        }
        return cIP;
    }
}
