package org.epzilla.dispatcher.clusterHandler;

import org.epzilla.clusterNode.rmi.ClusterInterface;
import org.epzilla.nameserver.NameService;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.net.MalformedURLException;

/**
 * Created by IntelliJ IDEA.
 * User: Team epZilla
 * Date: Mar 11, 2010
 * Time: 1:17:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class TriggerSender {
    static ClusterInterface clusterObj;
    public TriggerSender(){}

    public static void acceptTrigger(String serverIp,String clusterID,byte[] stream) throws MalformedURLException, NotBoundException, RemoteException {
        initCluster(serverIp,"Cluster");
        sendTriggers(stream,clusterID);
    }
    private static void initCluster(String serverIp,String serviceName) throws MalformedURLException, NotBoundException, RemoteException {
        String url = "rmi://"+serverIp+"/"+serviceName;
   		ClusterInterface obj = (ClusterInterface) Naming.lookup(url);
        setClusterObject(obj);

    }
    private static void sendTriggers(byte[] stream,String cID) throws RemoteException {
         clusterObj.acceptTiggerStream(stream,cID);
    }
    private static void setClusterObject(Object obj){
         clusterObj = (ClusterInterface) obj;
    }
    private static Object getClusterObject(){
        return clusterObj;
    }
}
