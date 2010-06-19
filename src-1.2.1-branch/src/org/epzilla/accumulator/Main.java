package org.epzilla.accumulator;


import org.epzilla.accumulator.service.AccumulatorService;
import org.epzilla.accumulator.service.AccumulatorServiceImpl;
import org.epzilla.accumulator.stm.AccumulatorAsClient;
import org.epzilla.accumulator.stm.AccumulatorAsServer;
import org.epzilla.accumulator.userinterface.AccumulatorUIControler;
import org.epzilla.accumulator.util.OpenSecurityManager;
import org.epzilla.util.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.Naming;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Feb 28, 2010
 * Time: 8:40:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    private static boolean isServer = true;
    private static String SERVICE_NAME = "ACCUMULATOR_SERVICE";
    private static int SLEEP_TIME = 1000;

    private static void startRegistry() {
        try {
            Runtime.getRuntime().exec("rmiregistry");
            Thread.sleep(SLEEP_TIME);
        }
        catch (IOException ex) {
            Logger.error("", ex);
        }
        catch (InterruptedException exc) {
            Logger.error("", exc);
        }
    }

    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new OpenSecurityManager());
        }

        startRegistry();
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            String ipAddress = inetAddress.getHostAddress();
            String url = "rmi://" + ipAddress + "/" + SERVICE_NAME;
            AccumulatorService obj = new AccumulatorServiceImpl();
            Naming.rebind(url, obj);
            Logger.log("Accumulator Service successfully deployed");
        } catch (Exception e) {
            Logger.error("", e);
        }

        AccumulatorUIControler.InitializeUI();
        if (isServer) {
            AccumulatorAsServer.startServer();
            AccumulatorAsServer.loadIPList();
        } else {
            AccumulatorAsClient.startClient();
        }

    }
}