package org.epzilla.dispatcher.rmi;

import net.epzilla.stratification.dynamic.SystemVariables;
import net.epzilla.stratification.restruct.RestructuringDaemon;
import org.epzilla.client.rmi.ClientCallbackInterface;
import org.epzilla.dispatcher.clusterHandler.TriggerSender;
import org.epzilla.dispatcher.dataManager.*;
import org.epzilla.dispatcher.dispatcherObjectModel.TriggerInfoObject;
import org.epzilla.dispatcher.logs.ReadLog;
import org.epzilla.dispatcher.ui.ClusterPerformanceData;
import org.epzilla.leader.LeaderElectionInitiator;
import org.epzilla.util.Logger;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
/**
 * Created by IntelliJ IDEA.
 * This is the Implementation of the DispInterface class
 * Author: Chathura
 * To change this template use File | Settings | File Templates.
 */
public class DispImpl extends UnicastRemoteObject implements DispInterface {

    private Vector<ClientCallbackInterface> clientList = new Vector<ClientCallbackInterface>();
    private HashMap clientMap = new HashMap<String, String>();
    private String clientIP;
    private static boolean isIDGen = false;
    private static int eventsSeqID = 1;
    private static String dispID = "";

    public DispImpl() throws RemoteException {

    }

    /**
     * This method will take the Events from client
     * @param event
     * @param clientID
     * @param eventSeqID
     * @return
     * @throws RemoteException
     */
    @Override
    public String uploadEventsToDispatcher(String event, String clientID, int eventSeqID) throws RemoteException {
        if (!isIDGen) {
            setDispatcherID();
        }
        try {
            if (RestructuringDaemon.isRestructuring()) {
                return "RETRY";
            } else {
                EventsCounter.setInEventCount();
                String eventIn = event + ":" + clientID + ":" + eventsSeqID + getDispatcherID();
                byte[] eventBuff = eventIn.getBytes();
                EventManager.sendEvents(eventBuff, clientID);
                eventsSeqID++;
                return "OK";
            }
        } catch (Exception e) {
            Logger.error("event accepting error", e);
        }
        return null;
    }

    /**
     * Method receive the Triggers from the client and added to the STM
     * @param tList
     * @param clientID
     * @param triggerSeqID
     * @return
     * @throws RemoteException
     */
    @Override
    public String uploadTriggersToDispatcher(ArrayList<String> tList, String clientID, int triggerSeqID) throws RemoteException {
        String toReturn = null;
        try {
            // todo disable accepting queries while restructuring.
//            if (RestructuringDaemon.isRestructuring()) {
//                return "RETRY";
//            } else {
            for (String x : tList) {
                Logger.log(x);
            }

            TriggerManager.addAllTriggersToList(tList, clientID);
            toReturn = "OK";
//            }
        } catch (Exception e) {
            Logger.error("trigger accepting error", e);
        }
        return toReturn;
    }

    /**
     * Method delete the triggers as requested by client
     * @param list
     * @param cID
     * @return
     * @throws RemoteException
     */
    @Override
    public String deleteTriggers(ArrayList<TriggerRepresentation> list, String cID) throws RemoteException {
        try {
            ArrayList<TriggerInfoObject> al = new ArrayList();
            for (TriggerInfoObject tio : TriggerManager.getTriggers()) {
                for (TriggerRepresentation tr : list) {
                    if (tr.getTriggerId().equals(tio.gettriggerID())) {
                        al.add(tio);
                    }
                }
            }

            Hashtable<Integer, String> ipset = new Hashtable(LeaderElectionInitiator.getSubscribedClusterLeadersFromAnyDispatcher());

            for (Integer id : ipset.keySet()) {
                TriggerSender.requestTriggerDeletion(ipset.get(id), Integer.toString(id), list, clientIP);
            }

            TriggerManager.getTriggers().removeAll(al);
            return "OK";
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return "RETRY";
    }

    /**
     * This method get all the triggers as requestd by client
     * @param clientId
     * @return
     * @throws RemoteException
     */
    @Override
    public ArrayList<TriggerRepresentation> getAllTriggers(String clientId) throws RemoteException {
        try {
            ArrayList<TriggerRepresentation> list = new ArrayList();
            for (TriggerInfoObject tio : TriggerManager.getTriggers()) {
                if (clientId.equals(tio.getclientID())) {
                    TriggerRepresentation tr = new TriggerRepresentation();
                    tr.setTrigger(tio.gettrigger());
                    tr.setTriggerId(tio.gettriggerID());
                    list.add(tr);
                }
            }
            return list;
        } catch (Exception e) {
            Logger.error("", e);
        }
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
        ArrayList<String> recoverTriggers = (ArrayList<String>) ReadLog.readLog(clusterID);
    }

    @Override
    public void registerClients(String ip, String id) throws RemoteException {
        clientMap.put(id, ip);
        ClientManager.addClient(id, ip);
    }

    @Override
    public void unRegisterClients(String ip, String id) throws RemoteException, MalformedURLException, UnknownHostException, NotBoundException {
        clientMap.remove(id);
        ClientManager.removeClient(id);
        DispLoadBalance.updateDecLoad();
    }

    @Override
    public String getClientIP(String clientID) throws RemoteException {
        return ClientManager.getClientIp(clientID);
    }

    /**
     * This method receive the performance information from the cluster leaders and add to the STM
     * @param clusterID
     * @param cpuUsg
     * @param mmUsg
     * @throws RemoteException
     */
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
            Logger.error("Cluster Leader IP:", e);
        }
    }

    /**
     * This method set the Dispatcher ID
     */
    public static void setDispatcherID() {
        try {
            InetAddress inetAddress;

            inetAddress = InetAddress.getLocalHost();
            String ipAddress = inetAddress.getHostAddress();
            String dispatcherID = idGenerator(ipAddress);
            dispID = dispatcherID;
            isIDGen = true;
        } catch (UnknownHostException e) {
            Logger.error("Generating Dispatcher ID:", e);
        }

    }

    public String getDispatcherID() {
        return dispID;
    }

    /**
     * This method generate Dispatcher ID from the IP address of the Dispatcher
     * @param ipAddress
     * @return
     */
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
