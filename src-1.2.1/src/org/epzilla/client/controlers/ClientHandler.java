package org.epzilla.client.controlers;

import org.epzilla.dispatcher.rmi.DispInterface;
import org.epzilla.nameserver.NameService;
import org.epzilla.client.rmi.ClientCallbackImpl;
import org.epzilla.client.rmi.ClientCallbackInterface;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Vector;

public class ClientHandler {

    private static Vector<String> dispIP = new Vector<String>();
    private static String clientID = "";
    private static String dispDetails = "";
    private static ClientCallbackInterface obj;
    private static NameService service;
    private static DispInterface disObj;
    private boolean isDispatcherInit = false;

    public static Vector<String> getServiceIp(String serverIp, String serviceName, String clientIp) throws MalformedURLException, RemoteException, NotBoundException {
        dispIP.removeAllElements();
        initNameService(serverIp, serviceName);
        dispDetails = service.getDispatcherIP();
        dispIP.add(dispDetails);
//		int size = service.getDirectorySize();
//		for(int i=0; i<size;i++){
//			ip = service.getHostName(i);
//			dispServiceName = service.getNames(i);
//			dispDetails=ip+" "+dispServiceName;
//			dispIP.add(dispDetails);
        System.out.println(dispIP);
        return dispIP;
    }

    public static String getClientsID(String ip) throws RemoteException {
        clientID = service.getClientID(ip);
        return clientID;
    }

    public void regForCallback(String ip, String serviceName) throws NotBoundException, RemoteException, MalformedURLException, UnknownHostException {
        if (!isDispatcherInit) {
            initDispatcher(ip, serviceName);
            ClientCallbackInterface obj = new ClientCallbackImpl();
            disObj.registerCallback(obj);
            setClientObject(obj);
        } else if (isDispatcherInit) {
            ClientCallbackInterface obj = new ClientCallbackImpl();
            disObj.registerCallback(obj);
            setClientObject(obj);
        }
    }

    public void unregisterCallback(String ip, String serviceName) throws MalformedURLException, RemoteException, NotBoundException, UnknownHostException {
        disObj.unregisterCallback((ClientCallbackInterface) getclientObject());
    }

    public static void registerClient(String ip, String id) throws RemoteException {
        disObj.registerClients(ip, id);
    }

    public static void unRegisterClient(String ip, String id) throws RemoteException {
        disObj.unRegisterClients(ip, id);
    }

    private static void initNameService(String serverIp, String serviceName) throws MalformedURLException, RemoteException, NotBoundException {
        String url = "rmi://" + serverIp + "/" + serviceName;
        NameService r = (NameService) Naming.lookup(url);
        setNameServiceObj(r);
    }

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
