package net.epzilla.accumulator;

import net.epzilla.accumulator.service.AccumulatorService;
import net.epzilla.accumulator.service.AccumulatorServiceImpl;
import net.epzilla.accumulator.service.ServiceLoader;
import net.epzilla.accumulator.util.OpenSecurityManager;
import net.epzilla.accumulator.util.ConfigFileScanner;
import net.epzilla.accumulator.global.SourceEvent;
import net.epzilla.accumulator.global.Event;
import net.epzilla.accumulator.stm.StmServer;
import net.epzilla.accumulator.stm.StmClient;

import java.rmi.RemoteException;
import java.rmi.Naming;
import java.net.MalformedURLException;
import java.io.IOException;
import java.io.Serializable;

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
            System.out.println("accumulator service deployed.");



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
