package org.epzilla.accumulator;


import org.epzilla.accumulator.service.ServiceLoader;
import org.epzilla.accumulator.stm.StmClient;
import org.epzilla.accumulator.stm.StmServer;
import org.epzilla.accumulator.util.OpenSecurityManager;
import org.epzilla.util.Logger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Feb 28, 2010
 * Time: 8:40:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new OpenSecurityManager());
        }


        try {
//            AccumulatorService accService = new AccumulatorServiceImpl();
//            ConfigFileScanner cfp = new ConfigFileScanner("./src/rmiconfig.config");
//            Naming.rebind(cfp.getParameter("AccumulatorService"), accService);
            if (Variables.isLeader) {
                StmServer.start();
            }   else {
                StmClient.start();
            }

            ServiceLoader sl = new ServiceLoader();
            sl.autodeploy();
//            Logger.log("accumulator service deployed.");
//              org.epzilla.util.
              Logger.log("Accumulator service deployed");

        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
