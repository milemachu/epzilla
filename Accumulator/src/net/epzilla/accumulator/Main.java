package net.epzilla.accumulator;

import net.epzilla.accumulator.service.AccumulatorService;
import net.epzilla.accumulator.service.AccumulatorServiceImpl;
import net.epzilla.accumulator.util.OpenSecurityManager;
import net.epzilla.accumulator.util.ConfigFileScanner;
import net.epzilla.accumulator.global.SourceEvent;
import net.epzilla.accumulator.global.Event;

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
            AccumulatorService accService = new AccumulatorServiceImpl();
            ConfigFileScanner cfp = new ConfigFileScanner("./src/rmiconfig.config");
            Naming.rebind(cfp.getParameter("AccumulatorService"), accService);
            System.out.println("accumulator service deployed.");


        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
