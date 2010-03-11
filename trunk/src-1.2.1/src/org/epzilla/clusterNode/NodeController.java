package org.epzilla.clusterNode;

import org.epzilla.clusterNode.userInterface.NodeUIController;
import org.epzilla.clusterNode.sharedMemory.NodeAsLeader;
import org.epzilla.clusterNode.dataManager.TriggerManager;

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
        NodeAsLeader.startServer();
        NodeAsLeader.loadTriggers();
//        TriggerManager.initTestTriggerStream();    For testing ONLY
    }

    public static void init() {
        NodeUIController.InitializeUI();
        NodeAsLeader.startServer();
        NodeAsLeader.loadTriggers();
    }
}
