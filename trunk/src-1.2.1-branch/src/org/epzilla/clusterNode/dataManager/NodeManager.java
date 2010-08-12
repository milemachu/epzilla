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
