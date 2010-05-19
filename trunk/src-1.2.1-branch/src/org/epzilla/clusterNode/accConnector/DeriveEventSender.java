package org.epzilla.clusterNode.accConnector;

import org.epzilla.accumulator.service.AccumulatorService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: Randika
 * Date: May 15, 2010
 * Time: 7:15:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class DeriveEventSender {
    private static AccumulatorService accObj;
    private static String response = null;
    private static Hashtable<String, Object> accList = new Hashtable<String, Object>();

    public DeriveEventSender() {
    }

    public static void sendDeriveEvent(String serverIP, byte[] deriveEvent) throws MalformedURLException, NotBoundException, RemoteException {

        if (!accList.containsKey(serverIP)) {
            initAccumulator(serverIP, "ACCUMULATOR_SERVICE");
            accObj.receiveDeriveEvent(deriveEvent);
        } else {
            accObj = (AccumulatorService) accList.get(serverIP);
            accObj.receiveDeriveEvent(deriveEvent);
        }

    }

    private static void initAccumulator(String serverIP, String serviceName) throws MalformedURLException, NotBoundException, RemoteException {
        String url = "rmi://" + serverIP + "/" + serviceName;
        AccumulatorService obj = (AccumulatorService) Naming.lookup(url);
        setAccObject(obj);
        accList.put(serverIP, getAccObject());

    }

    private static void setAccObject(Object obj) {
        accObj = (AccumulatorService) obj;
    }

    private static Object getAccObject() {
        return accObj;
    }
}
