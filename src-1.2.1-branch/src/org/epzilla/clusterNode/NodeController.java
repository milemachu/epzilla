package org.epzilla.clusterNode;

import org.epzilla.clusterNode.userInterface.IpListManager;
import org.epzilla.clusterNode.userInterface.NodeUIController;
import org.epzilla.clusterNode.replayLogs.sharedMemory.NodeAsLeader;
import org.epzilla.clusterNode.replayLogs.sharedMemory.NodeAsNonLeader;
import org.epzilla.clusterNode.loadAnalyzer.CpuMemAnalyzer;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: Mar 3, 2010
 * Time: 9:10:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class NodeController {
    private static int port = 4444;
    private static String leaderIP = "localhost";
    private static String thisIP = "localhost";
    private static boolean isLeader = true;

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        NodeController.port = port;
    }

    public static String getLeaderIP() {
        return leaderIP;
    }

    public static void setLeaderIP(String leaderIP) {
        NodeController.leaderIP = leaderIP;
    }

    public static void initUI() {
        NodeUIController.InitializeUI();
        CpuMemAnalyzer.Initialize();
        IpListManager.Initialize();
    }

    public static void setUiVisible() {
        NodeUIController.setVisible(true);
    }


    public static void initSTM() {
        if (isLeader) {
            NodeAsLeader.startServer();
            NodeAsLeader.loadTriggers();
            NodeAsLeader.loadIPList();
            NodeAsLeader.loadPerformanceInfoList();
            NodeAsLeader.checkForOverloading();
            //For testing ONLY
            //TriggerManager.initTestTriggerStream();
            //PerformanceInfoManager.initTestPerformanceInfoStream();
        } else {
            boolean success = NodeAsNonLeader.startClient();
            while (!success) {
                try {
                    Thread.sleep(4000);
                    success = NodeAsNonLeader.startClient();
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            }
        }
    }

    public static boolean isLeader() {
        return isLeader;
    }

    public static void setLeader(boolean leader) {
        isLeader = leader;
    }

    public static String getThisIP() {
        return thisIP;
    }

    public static void setThisIP(String thisIP) {
        NodeController.thisIP = thisIP;
    }
}
