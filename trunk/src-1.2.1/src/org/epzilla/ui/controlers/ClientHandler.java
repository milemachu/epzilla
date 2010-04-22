package org.epzilla.ui.controlers;

import org.epzilla.dispatcher.rmi.DispInterface;
import org.epzilla.nameserver.NameService;
import org.epzilla.ui.rmi.ClientCallbackImpl;
import org.epzilla.ui.rmi.ClientCallbackInterface;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Vector;

public class ClientHandler {

    InetAddress dispatcher;
    int dispatcherPort;
    static Vector<String> dispIP = new Vector<String>();
    String ip = "";
    static String clientID = "";
    String dispServiceName = "";
    static String dispDetails = "";
    static ClientCallbackInterface obj;
    static NameService service;
    static DispInterface disObj;
    boolean isDispatcherInit = false;

    public static Vector<String> getServiceIp(String serverIp, String serviceName, String clientIp) throws MalformedURLException, RemoteException, NotBoundException {
        initNameService(serverIp, serviceName);
        clientID = clientIdGen(clientIp);
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

    public void regForCallback(String ip, String serviceName) throws NotBoundException, RemoteException, MalformedURLException, UnknownHostException {
        if (isDispatcherInit == false) {
            initDispatcherInter(ip, serviceName);
            ClientCallbackInterface obj = new ClientCallbackImpl();
            disObj.registerCallback(obj);
            setClientObject(obj);
        } else if (isDispatcherInit == true) {
            ClientCallbackInterface obj = new ClientCallbackImpl();
            disObj.registerCallback(obj);
            setClientObject(obj);
        }
    }

    public void unregisterCallback(String ip, String serviceName) throws MalformedURLException, RemoteException, NotBoundException {
        disObj.unregisterCallback((ClientCallbackInterface) getclientObject());
    }

    private static String clientIdGen(String addr) {
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

    private static void initNameService(String serverIp, String serviceName) throws MalformedURLException, RemoteException, NotBoundException {
        String url = "rmi://" + serverIp + "/" + serviceName;
        NameService r = (NameService) Naming.lookup(url);
        setNameServiceObj(r);
    }

    private void initDispatcherInter(String ip, String serviceName) throws MalformedURLException, RemoteException, NotBoundException {
        String url = "rmi://" + ip + "/" + serviceName;
        DispInterface di = (DispInterface) Naming.lookup(url);
        setDispatcherObj(di);
    }

    public void setClientObject(Object objClient) {
        obj = (ClientCallbackInterface) objClient;
    }

    public Object getclientObject() {
        return obj;
    }

    public static void setNameServiceObj(Object objService) {
        service = (NameService) objService;
    }

    public Object getNameSerObj() {
        return service;
    }

    public void setDispatcherObj(Object obj) {
        disObj = (DispInterface) obj;
    }

    public Object getDispatcherObj() {
        return disObj;
    }

}
