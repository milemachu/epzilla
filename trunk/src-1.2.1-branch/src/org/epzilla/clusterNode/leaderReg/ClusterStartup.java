package org.epzilla.clusterNode.leaderReg;

import org.epzilla.clusterNode.NodeController;
import org.epzilla.clusterNode.rmi.ClusterImpl;
import org.epzilla.clusterNode.rmi.ClusterInterface;
import org.epzilla.clusterNode.userInterface.NodeUIController;
import org.epzilla.clusterNode.xml.ClusterSettingsReader;
import org.epzilla.dispatcher.rmi.DispInterface;
import org.epzilla.leader.LeaderElectionInitiator;
import org.epzilla.util.Logger;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Iterator;
/**
 * Created by IntelliJ IDEA.
 * Startup class for the Node
 * Author: Chathura
 * Date: May 15, 2010
 * Time: 7:15:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClusterStartup {
    private static ClusterSettingsReader reader = new ClusterSettingsReader();
    private static int clusterID;
    private static String nodeStatus;
    private static String serviceName = "CLUSTER_NODE";
    private static String dispatcherName = "DISPATCHER_SERVICE";
    private static DispInterface disObj;
    private static String ipAddress;
    private static LeaderElectionInitiator leaderElectionInitiator;
   /*
   * bind the Node to its own RMI registry
   */
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

    /*
    * register a Node in the Dispathcer before sending Performance informations
    */
    private static void register(String ip) throws RemoteException, MalformedURLException, NotBoundException, UnknownHostException {
        String id = dispIdGen(ip);
        String url = "rmi://" + ip + "/" + dispatcherName + id;
        DispInterface service;
        service = (DispInterface) Naming.lookup(url);
//        InetAddress inetAddress = InetAddress.getLocalHost();
//        String ipAddress = inetAddress.getHostAddress();
//        service.getLeaderIp(clusterID, ipAddress);      //cluster ID taken from the setting file clusterID_settings
        setDispObject(service);

        //DD for Client
//        org.epzilla.common.discovery.node.NodeDiscoveryManager nodeDiscMgr=new NodeDiscoveryManager(2);
//        NodeDiscoveryManager.setLeader(true);
//        NodeDiscoveryManager.setClusterLeader(InetAddress.getLocalHost().getHostAddress());
    }
    /*
    * generate the Dispatcher ID from the Dispatcher IP
    */
    private static String dispIdGen(String addr) {
        String[] addrArray = addr.split("\\.");
        String temp = "";
        String value = "";
        for (int i = 0; i < addrArray.length; i++) {
            temp = addrArray[i].toString();
            while (temp.length() != 3) {
                temp = '0' + temp;
            }
            value += temp;
        }
        return value;
    }

    public static void setDispObject(Object obj) {
        disObj = (DispInterface) obj;
    }

    public static Object getDispObject() {
        return disObj;
    }

    /*
    method to send performance info
     */
    public static void sendInfo(int cpuUsg, int mmUsg) {

        HashSet<String> leader = LeaderElectionInitiator.getDispatchers();
        clusterID = LeaderElectionInitiator.getClusterId();
        Iterator it = leader.iterator();
        while (it.hasNext()) {
            try {
                register((String) it.next());
                disObj.performanceInfo(clusterID, cpuUsg, mmUsg);   //cluster ID taken from the setting file clusterID_settings
            } catch (RemoteException e) {
                Logger.error("", e);
            } catch (MalformedURLException e) {
                Logger.error("", e);
            } catch (NotBoundException e) {
                Logger.error("", e);
            } catch (UnknownHostException e) {
                Logger.error("", e);
            }
        }
    }

    private static void loadSettings() {
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getLocalHost();
            ipAddress = inetAddress.getHostAddress();
            NodeController.setThisIP(ipAddress);
        } catch (UnknownHostException e) {
            Logger.error("", e);
        }
    }

    public static void main(String[] args) {
        try {
            bindClusterNode(serviceName);
        } catch (UnknownHostException e) {
            Logger.error("", e);
        } catch (MalformedURLException e) {
            Logger.error("", e);
        } catch (RemoteException e) {
            Logger.error("", e);
        }
        loadSettings();
//            if (nodeStatus == "default") {
//                initSTM();
//            }

        LeaderElectionInitiator.mainMethod();
        NodeController.initUI();
        startSTM();
    }

    public static boolean triggerLEFromRemote() {
        return LeaderElectionInitiator.initiateLeaderElection();
    }


    public static void startSTM() {
//        LeaderElectionInitiator.mainMethod();
        String leader = "";
        while (leader.equalsIgnoreCase("")) {
            leader = LeaderElectionInitiator.getLeader();
        }
        if (leader.equalsIgnoreCase(ipAddress)) {
            NodeController.setLeader(true);
            NodeController.setLeaderIP(ipAddress);  //set the ip address of the leader
            NodeController.setUiVisible();
            NodeController.initSTM();
            NodeUIController.setAddNodeButtonStatus(true);
            NodeUIController.setRemoveNodeBtnStatus(true);

        } else {
            NodeController.setLeader(false);
            NodeController.setLeaderIP(leader);
            NodeController.setUiVisible();
            NodeController.initSTM();
            NodeUIController.setAddNodeButtonStatus(false);
            NodeUIController.setRemoveNodeBtnStatus(false);
        }

    }

}
