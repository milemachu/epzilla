package org.epzilla.dispatcher.dataManager;

import org.epzilla.dispatcher.DispatcherUI;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: Feb 19, 2010
 * Time: 9:02:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class NodeVariables {
    private static int port = 4444;
    private static String nodeIP = "localhost";
    private static String currentServerIP = "192.168.1.3";
    private static DispatcherUI mainUI;

    public static String getNodeIP() {
        return nodeIP;
    }

    public static void setNodeIP(String nodeIP) {
        NodeVariables.nodeIP = nodeIP;
    }

    public static String getCurrentServerIP() {
        return currentServerIP;
    }

    public static void setCurrentServerIP(String currentServerIP) {
        NodeVariables.currentServerIP = currentServerIP;
    }

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        NodeVariables.port = port;
    }


    public static DispatcherUI getMainUI() {
        return mainUI;
    }

    public static void setMainUI(DispatcherUI mainUI) {
        NodeVariables.mainUI = mainUI;
    }
}
