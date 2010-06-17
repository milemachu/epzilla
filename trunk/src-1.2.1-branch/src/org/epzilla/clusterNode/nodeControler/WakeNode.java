package org.epzilla.clusterNode.nodeControler;

import org.epzilla.clusterNode.rmi.ClusterInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: chathura
 * Date: May 25, 2010
 * Time: 1:45:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class WakeNode { 

    public static void nodeInit(String nodeIP) {
        try {
            ClusterInterface nodeObj = initService(nodeIP, "CLUSTER_NODE");
            nodeObj.initNodeProcess();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static ClusterInterface initService(String serverIp, String serviceName) throws MalformedURLException, NotBoundException, RemoteException {
        String url = "rmi://" + serverIp + "/" + serviceName;
        return (ClusterInterface) Naming.lookup(url);
    }
}
