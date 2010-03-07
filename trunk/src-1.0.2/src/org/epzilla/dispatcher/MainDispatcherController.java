package org.epzilla.dispatcher;

import org.epzilla.dispatcher.sharedMemoryModule.DispatcherAsServer;
import org.epzilla.dispatcher.sharedMemoryModule.DispatcherAsClient;
import org.epzilla.dispatcher.dataManager.ClusterLeaderIpListManager;
import org.epzilla.dispatcher.dataManager.TriggerManager;
import org.epzilla.dispatcher.dataManager.NodeVariables;


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
        try {
            DispatcherUIController.InitializeUI();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        runAsServer();

    }

    public static void runAsServer() {
        RandomStringGenerator.generate(1000);
       boolean IsSuccessful = DispatcherAsServer.startServer();
        if(IsSuccessful)
        {
        DispatcherAsServer.loadTriggers();
        DispatcherAsServer.loadIPList();
        DispatcherAsServer.loadClientList();
        TriggerManager.initTestTriggerStream();
        ClusterLeaderIpListManager.loadSampleIPs();
        }

    }

    private static void runAsClient() {
        DispatcherAsClient.startClient();
    }


}
