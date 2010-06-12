package org.epzilla.clusterNode.nodeControler;

import org.epzilla.clusterNode.rmi.ClusterInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: Chathura
 * Date: Jun 11, 2010
 * Time: 12:05:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class NodeDown {
    public static void sleepNode(String nodeIP) { 
        try {
            ClusterInterface clusterObj = initService(nodeIP, "CLUSTER_NODE");
            clusterObj.sleepNodeProcess();
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
