package org.epzilla.dispatcher;

import jstm.misc.Console;
import org.epzilla.dispatcher.sharedMemoryModule.DispatcherAsServer;
import org.epzilla.dispatcher.sharedMemoryModule.DispatcherAsClient;


import javax.swing.*;
import java.net.UnknownHostException;
import java.net.InetAddress;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: Feb 18, 2010
 * Time: 3:33:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class MainDispatcherController {


    public static void main(String[] args) {
        try {
            InetAddress addr = InetAddress.getLocalHost();

            // Get IP Address
            String ipAddr = addr.getHostAddress();
            NodeVariables.setNodeIP(ipAddr);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        run();
    }

    private static void run() {
        DispatcherUIController.InitializeUI();
        runAsServer();

    }

    public static void runAsServer() {
        RandomStringGenerator.generate(1000);
        DispatcherAsServer.startServer();
        DispatcherAsServer.loadTriggers();
//        DispatcherAsServer.loadIPList();
        DispatcherAsServer.loadClientList();
        TriggerManager.acceptTriggerStream();
//        ClusterLeaderIpListManager.loadSampleIPs();

    }

    private static void runAsClient() {
        DispatcherAsClient.startClient();

    }


}
