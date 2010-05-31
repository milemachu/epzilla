package org.epzilla.dispatcher.rmi;

import net.epzilla.stratification.dynamic.SystemVariables;
import net.epzilla.stratification.restruct.RestructuringDaemon;
import org.epzilla.client.rmi.ClientCallbackInterface;
import org.epzilla.dispatcher.controlers.DispatcherUIController;
import org.epzilla.dispatcher.dataManager.*;
import org.epzilla.dispatcher.logs.ReadLog;
import org.epzilla.dispatcher.ui.ClusterPerformanceData;
import org.epzilla.util.Logger;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class DispImpl extends UnicastRemoteObject implements DispInterface {

    private Vector<ClientCallbackInterface> clientList = new Vector<ClientCallbackInterface>();
    private HashMap clientMap = new HashMap<String, String>();
    private ArrayList<String> recoverTriggers = new ArrayList<String>();
    private String clientIP;
    private static boolean isIDGen = false;
    private static int eventsSeqID = 1;
    private static String dispID = "";

    protected DispImpl() throws RemoteException {

    }

    @Override
    public String uploadEventsToDispatcher(String event, String clientID, int eventSeqID) throws RemoteException {
        if (!isIDGen) {
            setDispatcherID();
        }
        try {
            EventsCounter.setInEventCount();
            String eventIn = event + ":" + clientID + ":" + eventsSeqID + getDispatcherID();
            byte[] eventBuff = eventIn.getBytes();
            EventManager.sendEvents(eventBuff, clientID);
            eventsSeqID++;
            return "OK";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String uploadTriggersToDispatcher(ArrayList<String> tList, String clientID, int triggerSeqID) throws RemoteException {
        String toReturn = null;
        try {
            for (String x : tList) {
                Logger.log(x);
            }

            TriggerManager.addAllTriggersToList(tList, clientID);
            toReturn = "OK";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    @Override
    public String deleteTriggers(ArrayList<String> list, String cID, int triggerSeqID) throws RemoteException {

        return null;
    }

    @Override
    public void getNotifications(String notification, String clientID) throws RemoteException {
        clientIP = getClientIP(clientID);

    }

    @Override
    public void registerCallback(ClientCallbackInterface clientObject) throws RemoteException, MalformedURLException, UnknownHostException, NotBoundException {
        DispLoadBalance.updateIncLoad();
        if (!(clientList.contains(clientObject))) {
            clientList.addElement(clientObject);
            Logger.log("Registered new client " + clientObject);
        }
    }

    @Override
    public void unregisterCallback(ClientCallbackInterface clientObject) throws RemoteException, MalformedURLException, UnknownHostException, NotBoundException {
        DispLoadBalance.updateDecLoad();
        if (clientList.removeElement(clientObject)) {
            Logger.log("Unregistered the client ");
        } else {
            Logger.log(
                    "unregister: clientwasn't registered." + clientObject);
        }
    }

    @Override
    public void replayLogs(String clusterID, String leaderIP) throws RemoteException {
        recoverTriggers = (ArrayList<String>) ReadLog.readLog(clusterID);
    }

    @Override
    public void registerClients(String ip, String id) throws RemoteException {
        clientMap.put(id, ip);
        ClientManager.addClient(id, ip);
    }

    @Override
    public void unRegisterClients(String ip, String id) throws RemoteException {
        clientMap.remove(id);
        ClientManager.removeClient(id);
    }

    @Override
    public String getClientIP(String clientID) throws RemoteException {
        return ClientManager.getClientIp(clientID);
    }

    @Override
    public void performanceInfo(int clusterID, int cpuUsg, int mmUsg) throws RemoteException {
        int load = (cpuUsg + mmUsg) / 2;
        SystemVariables.setClusterLoad(0, clusterID, load);
        // todo - change if needed.
        ClusterPerformanceData.getInstance().addData(clusterID, cpuUsg, mmUsg);
//        DispatcherUIController.appendClusterData(""+clusterID,""+cpuUsg,""+mmUsg);
    }

    @Override
    public void restructuringStarted(RestructuringInfo ri) throws RemoteException {
        //To change body of implemented methods use File | Settings | File Templates.
        RestructuringDaemon.setRestructuring(true);
    }

    @Override
    public void restructuringEnded(RestructuringInfo ri) throws RemoteException {
        //To change body of implemented methods use File | Settings | File Templates.
        RestructuringDaemon.setRestructuring(false);

    }

    @Override
    public void getLeaderIp(int id, String ip) throws RemoteException {
        try {

            ClusterLeaderIpListManager.addIP("" + id, ip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setDispatcherID() {
        try {
            InetAddress inetAddress;

            inetAddress = InetAddress.getLocalHost();
            String ipAddress = inetAddress.getHostAddress();
            String dispatcherID = idGenerator(ipAddress);
            dispID = dispatcherID;
            isIDGen = true;
        } catch (UnknownHostException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public String getDispatcherID() {
        return dispID;
    }

    public static String idGenerator(String ipAddress) {
        String[] addrArray = ipAddress.split("\\.");
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

    public synchronized void callBacks(String clientID) throws RemoteException {
    }


}
