package org.epzilla.accumulator.dataManager;

import jstm.core.Site;
import jstm.core.TransactedList;
import jstm.core.Transaction;
import org.epzilla.accumulator.generated.PerformanceInfoObject;

import java.util.TimerTask;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: Jun 15, 2010
 * Time: 11:36:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class PerformanceInfoManager {
      private static TransactedList<PerformanceInfoObject> performanceList = new TransactedList<PerformanceInfoObject>();


    // Code For Testing Only -Dishan

    public static void initTestPerformanceInfoStream() {
        final java.util.Timer timer1 = new java.util.Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                addPerformanceInfo("127.0.0.1", "20", "50", "10");
            }


        }, 0, 500);

        final java.util.Timer timer2 = new java.util.Timer();
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                removePerformanceInfoFromList("127.0.0.1");
            }
        }, 2000, 1500);

    }


    public static boolean addPerformanceInfo(String nodeIP, String cpuUsage, String memUsage, String networkUsage) {
        boolean success = false;
        if (getPerformanceList() != null) {
            synchronized (performanceList) {
                if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
                    Site.getLocal().allowThread();
                    Transaction transaction1 = Site.getLocal().startTransaction();
                    PerformanceInfoObject obj = new PerformanceInfoObject();
                    obj.setnodeIP(nodeIP);
                    obj.setCPUusageAverage(cpuUsage);
                    obj.setMemUsageAverage(memUsage);
                    obj.setNetworkUsageAverage(networkUsage);
                    performanceList.add(obj);
                    transaction1.commit();
                    success = true;
                }
            }
        }
        return success;
    }


    public static boolean removePerformanceInfoFromList(String nodeIP) {
        boolean success = false;
        if (getPerformanceList() != null) {
            try {
                synchronized (performanceList) {
                    if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
                        Site.getLocal().allowThread();
                        for (int i = 0; i < performanceList.size(); i++) {
                            if (performanceList.get(i).getnodeIP().equalsIgnoreCase(nodeIP)) {
                                Transaction transaction2 = Site.getLocal().startTransaction();
                                performanceList.remove(i);
                                transaction2.beginCommit(null);
                                success = true;
                                break;

                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    public static TransactedList<PerformanceInfoObject> getPerformanceList() {
        return performanceList;
    }

    public static void setPerformanceList(TransactedList<PerformanceInfoObject> performanceList) {
        PerformanceInfoManager.performanceList = performanceList;
    }
}
