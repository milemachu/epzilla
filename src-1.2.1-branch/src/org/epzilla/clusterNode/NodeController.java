package org.epzilla.clusterNode;

import org.epzilla.clusterNode.userInterface.NodeUIController;
import org.epzilla.clusterNode.sharedMemory.NodeAsLeader;
import org.epzilla.clusterNode.sharedMemory.NodeAsNonLeader;
import org.epzilla.clusterNode.dataManager.TriggerManager;
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

    public static void main(String[] args) {
        NodeUIController.InitializeUI();
        CpuMemAnalyzer.Initialize();
        if (isLeader) {
            NodeAsLeader.startServer();
            NodeAsLeader.loadTriggers();
            NodeAsLeader.loadIPList();
            NodeAsLeader.loadPerformanceInfoList();
            NodeAsLeader.checkForOverloading();
        } else {
            NodeAsNonLeader.startClient();
        }
        //TriggerManager.initTestTriggerStream();   // For testing ONLY
    }

    public static void init() {
        NodeUIController.InitializeUI();
        CpuMemAnalyzer.Initialize();
        if (isLeader) {
            NodeAsLeader.startServer();
            NodeAsLeader.loadTriggers();
            NodeAsLeader.loadIPList();
            NodeAsLeader.loadPerformanceInfoList();
            NodeAsLeader.checkForOverloading();
        } else {
            NodeAsNonLeader.startClient();
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
