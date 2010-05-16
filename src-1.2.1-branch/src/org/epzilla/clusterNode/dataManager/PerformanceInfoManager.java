package org.epzilla.clusterNode.dataManager;

import org.epzilla.clusterNode.clusterInfoObjectModel.PerformanceInfoObject;

import jstm.core.TransactedList;
import jstm.core.Site;
import jstm.core.Transaction;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: May 15, 2010
 * Time: 9:22:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class PerformanceInfoManager {
    private static TransactedList<PerformanceInfoObject> performanceList = new TransactedList<PerformanceInfoObject>();

    public static void addPerformanceInfo(String nodeIP, String cpuUsage, String memUsage, String networkUsage) {
        if (getPerformanceList() != null) {
            if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
                Site.getLocal().allowThread();
                Transaction transaction = Site.getLocal().startTransaction();
                PerformanceInfoObject obj = new PerformanceInfoObject();
                obj.setnodeIP(nodeIP);
                obj.setCPUusageAverage(cpuUsage);
                obj.setMemUsageAverage(memUsage);
                obj.setNetworkUsageAverage(networkUsage);
                performanceList.add(obj);
                transaction.commit();
            }
        }
    }

      public static void removePerformanceObject(String nodeIP) {
            if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
                Site.getLocal().allowThread();
                Transaction transaction = Site.getLocal().startTransaction();
                for (int i = 0; i < performanceList.size(); i++) {
                    if (performanceList.get(i).getnodeIP().equals(nodeIP)) {
                        performanceList.remove(i);
                        break;
                    }
                }
                transaction.commit();
        }
    }


    public static TransactedList<PerformanceInfoObject> getPerformanceList() {
        return performanceList;
    }

    public static void setPerformanceList(TransactedList<PerformanceInfoObject> performanceList) {
        PerformanceInfoManager.performanceList = performanceList;
    }
}
