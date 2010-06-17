package org.epzilla.clusterNode.leaderReg;

import org.epzilla.clusterNode.NodeController;
import org.epzilla.clusterNode.rmi.ClusterImpl;
import org.epzilla.clusterNode.rmi.ClusterInterface;
import org.epzilla.clusterNode.xml.ClusterSettingsReader;
import org.epzilla.dispatcher.rmi.DispInterface;
import org.epzilla.util.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class LeaderRegister {
    private static ClusterSettingsReader reader = new ClusterSettingsReader();
    private static String clusterID = "";
    private static String serviceName = "CLUSTER_NODE";
    private static DispInterface disObj;

    public static void bindClusterNode(String serviceName) throws UnknownHostException, MalformedURLException, RemoteException {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new org.epzilla.clusterNode.leaderReg.OpenSecurityManager());
        }
        ClusterInterface clusterInt = new ClusterImpl();
        InetAddress inetAddress;
        inetAddress = InetAddress.getLocalHost();
        String ipAddress = inetAddress.getHostAddress();
        String name = "rmi://" + ipAddress + "/" + serviceName;
        Naming.rebind(name, clusterInt);
        Logger.log("Cluster Node successfully deployed.....");
    }

    private static void register() throws RemoteException, MalformedURLException, NotBoundException, UnknownHostException {
        String url = "rmi://" + "192.168.1.4" + "/" + "DISPATCHER_SERVICE192168001004";
        DispInterface service;
        service = (DispInterface) Naming.lookup(url);
        InetAddress inetAddress = InetAddress.getLocalHost();
        String ipAddress = inetAddress.getHostAddress();
        service.getLeaderIp(1, ipAddress);      //cluster ID HC
        setDispObject(service);


        //DD for Client
//        org.epzilla.common.discovery.node.NodeDiscoveryManager nodeDiscMgr=new NodeDiscoveryManager(2);
//        NodeDiscoveryManager.setLeader(true);
//        NodeDiscoveryManager.setClusterLeader(InetAddress.getLocalHost().getHostAddress());
    }

    public static void setDispObject(Object obj) {
        disObj = (DispInterface) obj;
    }

    public static Object getDispObject() {
        return disObj;
    }

    //method to send performance info
    public static void sendInfo(int cpuUsg, int mmUsg) {
        try {
            disObj.performanceInfo(1, cpuUsg, mmUsg);   //cluster ID HC
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static void loadSettings() {
        try {
            ArrayList<String[]> data = reader.getServerIPSettings("clusterID_settings.xml");
            String[] ar = data.get(0);
            clusterID = ar[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            NodeController.initUI();
            NodeController.initSTM();
            bindClusterNode(serviceName);
            loadSettings();
            register();
        } catch (UnknownHostException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NotBoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
