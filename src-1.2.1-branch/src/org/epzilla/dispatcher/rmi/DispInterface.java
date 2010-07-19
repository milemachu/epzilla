package org.epzilla.dispatcher.rmi;

import org.epzilla.client.rmi.ClientCallbackInterface;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * Dispatcher Interface class
 * To change this template use File | Settings | File Templates.
 */
public interface DispInterface extends Remote {
    /**
     * @param event
     * @param cID
     * @param eventSeqID
     * @return
     * @throws RemoteException
     */
    public String uploadEventsToDispatcher(String event, String cID, int eventSeqID) throws RemoteException;

    /**
     * @param tList
     * @param cID
     * @param triggerSeqID
     * @return
     * @throws RemoteException
     */
    public String uploadTriggersToDispatcher(ArrayList<String> tList, String cID, int triggerSeqID) throws RemoteException;

    /**
     * @param list
     * @param cID
     * @return
     * @throws RemoteException
     */
    public String deleteTriggers(ArrayList<TriggerRepresentation> list, String cID) throws RemoteException;

    /**
     * @param clientId
     * @return
     * @throws RemoteException
     */
    public ArrayList<TriggerRepresentation> getAllTriggers(String clientId) throws RemoteException;

    /**
     * @param notification
     * @param clientID
     * @throws RemoteException
     */
    public void getNotifications(String notification, String clientID) throws RemoteException;

    /**
     * @param clientObject
     * @throws RemoteException
     * @throws MalformedURLException
     * @throws UnknownHostException
     * @throws NotBoundException
     */
    public void registerCallback(ClientCallbackInterface clientObject) throws RemoteException, MalformedURLException, UnknownHostException, NotBoundException;

    /**
     * @param clientObject
     * @throws RemoteException
     * @throws MalformedURLException
     * @throws UnknownHostException
     * @throws NotBoundException
     */
    public void unregisterCallback(ClientCallbackInterface clientObject) throws RemoteException, MalformedURLException, UnknownHostException, NotBoundException;

    /**
     * @param id
     * @param ip
     * @throws RemoteException
     */
    public void getLeaderIp(int id, String ip) throws RemoteException;

    /**
     * @param clusterID
     * @param leaderIP
     * @throws RemoteException
     */
    public void replayLogs(String clusterID, String leaderIP) throws RemoteException;

    /**
     * @param ip
     * @param id
     * @throws RemoteException
     */
    public void registerClients(String ip, String id) throws RemoteException;

    /**
     * @param ip
     * @param id
     * @throws RemoteException
     * @throws MalformedURLException
     * @throws UnknownHostException
     * @throws NotBoundException
     */
    public void unRegisterClients(String ip, String id) throws RemoteException, MalformedURLException, UnknownHostException, NotBoundException;

    /**
     * @param clientID
     * @return
     * @throws RemoteException
     */
    public String getClientIP(String clientID) throws RemoteException;

    /**
     * @param clusterID
     * @param cpuUsg
     * @param mmUsg
     * @throws RemoteException
     */
    public void performanceInfo(int clusterID, int cpuUsg, int mmUsg) throws RemoteException;

    /**
     * @param ri
     * @throws RemoteException
     */
    public void restructuringStarted(RestructuringInfo ri) throws RemoteException;

    /**
     * @param ri
     * @throws RemoteException
     */
    public void restructuringEnded(RestructuringInfo ri) throws RemoteException;

}