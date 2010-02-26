package org.epzilla.dispatcher;

import jstm.misc.Console;
import org.epzilla.sharedMemoryModule.DispatcherAsServer;
import org.epzilla.sharedMemoryModule.DispatcherAsClient;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: Feb 18, 2010
 * Time: 3:33:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class MainDispatcherController {


    public static void main(String[] args) {
        run();
    }

    private static void run() {
        DispatcherUI form = new DispatcherUI();
        NodeVariables.setMainUI(form);
        form.setVisible(true);
        runAsServer();
    }

    public static void runAsServer() {
        RandomStringGenerator.generate(1000);

        DispatcherAsServer.startServer();
        Console.readLine();
        DispatcherAsServer.loadTriggers();
        DispatcherAsServer.loadIPList();
        TriggerManager.acceptTriggerStream();
        ClusterLeaderIpListManager.loadSampleIPs();
        Console.readLine();
    }

    private static void runAsClient() {
        DispatcherAsClient.startClient();
        Console.readLine();
    }


}
