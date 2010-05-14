package org.epzilla.nameserver;

import org.epzilla.nameserver.loadbalance.RBLoadBalancer;
import org.epzilla.nameserver.xmlLog.XmlReader;
import org.epzilla.nameserver.xmlLog.XmlWriter;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;
import java.util.Vector;

public class NameServiceImpl extends UnicastRemoteObject implements NameService {

    private int dirsize = 0;
    private static String clientID = "";
    private static Vector<String> dispatcherName = new Vector<String>();
    private static Vector<String> dispatcherIPAdrs = new Vector<String>();
    private static Vector<Integer> dispatcherPort = new Vector<Integer>();
    private static Hashtable<String, String> clientList = new Hashtable<String, String>();

    public NameServiceImpl() throws RemoteException {
    }

    public int getDirsize() {
        return dirsize;
    }

    private int searchDisp(String s) {
        for (int i = 0; i < dirsize; i++)
            if (dispatcherIPAdrs.elementAt(i).equalsIgnoreCase(s))
                return i;
        return -1;
    }

    public int insertNode(String name, String ipAdrs, int portNumber) throws RemoteException {
        int oldIndex = searchDisp(ipAdrs);
        if (oldIndex == -1) {
            dispatcherName.add(dirsize, name);
            dispatcherIPAdrs.add(dirsize, ipAdrs);
            dispatcherPort.add(dirsize, portNumber);
            dirsize++;
            RBLoadBalancer.insert(ipAdrs);
            XmlWriter.writeToFile(name, ipAdrs, portNumber);
            return 1;
        } else
            return 0;
    }

    public int getDirectorySize() throws RemoteException {
        return dirsize;
    }

    public String getDispatcherIP() throws RemoteException {
        String dispIP = RBLoadBalancer.getIPAddress();
        int dispID = searchDisp(dispIP);
        String toReturn = dispIP + " " + dispatcherName.elementAt(dispID);
        return toReturn;
    }

    public String getClientID(String ipAdrs) throws RemoteException {
        if (clientList.containsKey(ipAdrs)) {
            clientID = clientList.get(ipAdrs);
        } else {
            clientID = clientIdGen(ipAdrs);
            clientList.put(ipAdrs, clientID);
        }
        return clientID;
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

    private void loadDispDetails() {
        try {
            Vector<String[]> data = XmlReader.readFile("./src/org/epzilla/nameserver/xmlLog/dispatcherData.xml");
            for (int i = 0; i < data.size(); i++) {
                String[] ar = data.get(i);
                dispatcherName.add(ar[0]);
                dispatcherIPAdrs.add(ar[1]);
                dispatcherPort.add(Integer.valueOf(ar[2]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateIncLoad(String ip) throws RemoteException {
        RBLoadBalancer.updateInc(ip);
    }

    public void updateDecLoad(String ip) throws RemoteException {
        RBLoadBalancer.updateDec(ip);
    }
}
