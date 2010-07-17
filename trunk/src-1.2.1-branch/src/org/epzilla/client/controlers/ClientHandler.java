package org.epzilla.client.controlers;

import org.epzilla.client.rmi.ClientCallbackImpl;
import org.epzilla.client.rmi.ClientCallbackInterface;
import org.epzilla.dispatcher.rmi.DispInterface;
import org.epzilla.nameserver.NameService;
import org.epzilla.util.Logger;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Vector;
/**
 * Created by IntelliJ IDEA.
 * This class use to handle the client side operations, and creating RMI connections
 * to the NameServer and Dispatchers.
 * Author: Chathura
 * Date: Jan 18, 2010
 * Time: 11:12:36 PM
 */
public class ClientHandler {

    private static Vector<String> dispIP = new Vector<String>();
    private static String dispDetails = "";
    private static ClientCallbackInterface obj;
    private static NameService service;
    private static DispInterface disObj;

    /*
   This is to get Dispatcher IPs from the Name Server
    */
    public static Vector<String> getServiceIp(String serverIp, String serviceName, String clientIp) throws MalformedURLException, RemoteException, NotBoundException {
        dispIP.removeAllElements();
        initNameService(serverIp, serviceName);
        try {
            dispDetails = service.getDispatcherIP();
            if (!dispDetails.equals(" ")) {
                dispIP.add(dispDetails);
            }
        } catch (Exception e) {
            Logger.error("URL error:", e);
        }
        Logger.log(dispIP);
        return dispIP;
    }

    /*
   Get client id generated from the Name Server
    */
    public static String getClientsID(String ip) throws RemoteException {
        return service.getClientID(ip);
    }

    /*
    Client register for call backs from the Dispatcher
    */
    public void regForCallback(String ip, String serviceName) throws NotBoundException, RemoteException, MalformedURLException, UnknownHostException {
        boolean dispatcherInit = false;
        if (!dispatcherInit) {
            initDispatcher(ip, serviceName);
            ClientCallbackInterface obj = new ClientCallbackImpl();
            disObj.registerCallback(obj);
            setClientObject(obj);
        } else if (dispatcherInit) {
            ClientCallbackInterface obj = new ClientCallbackImpl();
            disObj.registerCallback(obj);
            setClientObject(obj);
        }
    }
    /*
   Client unregistering from call backs
    */

    public void unregisterCallback(String ip, String serviceName) throws MalformedURLException, RemoteException, NotBoundException, UnknownHostException {
        disObj.unregisterCallback((ClientCallbackInterface) getclientObject());
    }

    /*
   Client register in the Dispatcher
    */
    public static void registerClient(String ip, String id) throws RemoteException {
        disObj.registerClients(ip, id);
    }

    /*
   Client unregister from the Dispatcher
    */
    public static void unRegisterClient(String ip, String id) throws RemoteException, MalformedURLException, UnknownHostException, NotBoundException {
        disObj.unRegisterClients(ip, id);
    }

    /*
   Initialize the reference to the Name Service, create remote object
    */
    private static void initNameService(String serverIp, String serviceName) throws MalformedURLException, RemoteException, NotBoundException {
        String url = "rmi://" + serverIp + "/" + serviceName;
        NameService r = (NameService) Naming.lookup(url);
        setNameServiceObj(r);
    }

    /*
   Initialize Dispatcher service, create remote object
    */
    private static void initDispatcher(String ip, String serviceName) throws MalformedURLException, RemoteException, NotBoundException {
        String url = "rmi://" + ip + "/" + serviceName;
        DispInterface di = (DispInterface) Naming.lookup(url);
        setDispatcherObj(di);
    }

    public static void setClientObject(Object objClient) {
        obj = (ClientCallbackInterface) objClient;
    }

    public static Object getclientObject() {
        return obj;
    }

    public static void setNameServiceObj(Object objService) {
        service = (NameService) objService;
    }

    public static Object getNameSerObj() {
        return service;
    }

    public static void setDispatcherObj(Object obj) {
        disObj = (DispInterface) obj;
    }

    public static Object getDispatcherObj() {
        return disObj;
    }

}
