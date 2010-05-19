package org.epzilla.accumulator;


import org.epzilla.accumulator.service.AccumulatorService;
import org.epzilla.accumulator.service.AccumulatorServiceImpl;
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
    private static void startRegistry() {
        Process rmiProcess = null;
        try {
            rmiProcess = Runtime.getRuntime().exec("rmiregistry");
            Thread.sleep(1000);
        }
        catch (IOException ex) {
        }
        catch (InterruptedException exc) {
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
            String url = "rmi://" + ipAddress + "/" + "ACCUMULATOR_SERVICE";
            AccumulatorService obj = new AccumulatorServiceImpl();
            Naming.rebind(url, obj);
            Logger.log("Accumulator Service successfully deployed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}