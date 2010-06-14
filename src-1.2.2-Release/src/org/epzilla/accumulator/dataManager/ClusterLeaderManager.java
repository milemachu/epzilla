package org.epzilla.accumulator.dataManager;

import org.epzilla.accumulator.generated.LeaderInfoObject;
import jstm.core.TransactedList;
import jstm.core.Site;
import jstm.core.Transaction;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: May 23, 2010
 * Time: 8:07:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClusterLeaderManager {
    private static TransactedList<LeaderInfoObject> detailsList = new TransactedList<LeaderInfoObject>(20);


    public static TransactedList<LeaderInfoObject> getDetailsList() {
        return detailsList;
    }

    public static void setDetailsList(TransactedList<LeaderInfoObject> detailsList) {
        ClusterLeaderManager.detailsList = detailsList;
    }

    public static void addIP(String clusterID, String ip) {
        if (detailsList != null) {
            if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
                Site.getLocal().allowThread();
                Transaction transaction = Site.getLocal().startTransaction();
                LeaderInfoObject obj = new LeaderInfoObject();
                obj.setclusterID(clusterID);
                obj.setleaderIP(ip);
                detailsList.add(obj);
                transaction.commit();
            }
        }
    }

        public static void removeIP(String ip) {
        if (detailsList != null) {
            try {
                if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
                    Site.getLocal().allowThread();
                    Transaction transaction = Site.getLocal().startTransaction();
                    for (int i = 0; i < detailsList.size(); i++) {
                        if (detailsList.get(i).getleaderIP().equals(ip)) {
                            detailsList.remove(i);
                            break;
                        }
                    }
                    transaction.commit();

                }
            } catch (Exception e) {

            }
        }
    }


}
