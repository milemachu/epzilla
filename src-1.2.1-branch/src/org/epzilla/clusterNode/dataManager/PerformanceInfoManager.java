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

    public static boolean isObjectInList(String ip) {
        boolean result = false;
        for (int i = 0; i < performanceList.size(); i++) {
            if (performanceList.get(i).getnodeIP() == ip)
                result = true;
        }
        return result;
    }


    public static void removePerformanceObject(String nodeIP) {
        try {
            int count = performanceList.size();
            if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
                Site.getLocal().allowThread();
                int removeIndex = -1;
                for (int i = 0; i < count; i++) {
                    if (performanceList.get(i).getnodeIP().equals(nodeIP)) {
                        removeIndex = i;
                        break;
                    }
                }
                if (removeIndex != -1) {
                    Transaction transaction = Site.getLocal().startTransaction();
                    boolean result = getPerformanceList().remove(getPerformanceList().get(removeIndex));
                    if (result)
                        transaction.commit();
                    else
                        transaction.abort();
                }
            }
        }
        catch (Exception ex) {
        }
    }

    public static TransactedList<PerformanceInfoObject> getPerformanceList() {
        return performanceList;
    }

    public static void setPerformanceList(TransactedList<PerformanceInfoObject> performanceList) {
        PerformanceInfoManager.performanceList = performanceList;
    }
}
