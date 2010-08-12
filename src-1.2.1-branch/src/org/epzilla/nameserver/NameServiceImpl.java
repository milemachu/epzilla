/***************************************************************************************************
Copyright 2009 - 2010 Harshana Eranga Martin, Dishan Metihakwala, Rajeev Sampath, Chathura Randika

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
****************************************************************************************************/
package org.epzilla.nameserver;

import org.epzilla.nameserver.loadbalance.RBLoadBalancer;
import org.epzilla.nameserver.xmlLog.XmlReader;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;
import java.util.Vector;
/**
 * Created by IntelliJ IDEA.
 * This is the implementation class of the Name Server Interface
 * Author: Chathura
 * To change this template use File | Settings | File Templates.
 */
public class NameServiceImpl extends UnicastRemoteObject implements NameService {

    private int dirsize = 0;
    private static String clientID = "";
    private static Vector<String> dispatcherName = new Vector<String>();
    private static Vector<String> dispatcherIPAdrs = new Vector<String>();
    private static Vector<Integer> dispatcherPort = new Vector<Integer>();
    private static Hashtable<String, String> clientList = new Hashtable<String, String>();

    public NameServiceImpl() throws RemoteException {
    }
    /*
   search the dispatcher details are exist before registering in the name server
    */
    private int searchDisp(String s) {
        for (int i = 0; i < dirsize; i++)
            if (dispatcherIPAdrs.elementAt(i).equalsIgnoreCase(s))
                return i;
        return -1;
    }

    /*
   insert dispatcher detials
    */
    public int insertNode(String name, String ipAdrs, int portNumber) throws RemoteException {
        int oldIndex = searchDisp(ipAdrs);
        if (oldIndex == -1) {
            dispatcherName.add(dirsize, name);
            dispatcherIPAdrs.add(dirsize, ipAdrs);
            dispatcherPort.add(dirsize, portNumber);
            dirsize++;
            RBLoadBalancer.insert(ipAdrs);
            return 1;
        } else
            return 0;
    }

    /*
   get the number of dispatchers registered
    */
    public int getDirectorySize() throws RemoteException {
        return dirsize;
    }

    public String getDispatcherIP() throws RemoteException {
        String dispIP = RBLoadBalancer.getIPAddress();
        int dispID = searchDisp(dispIP);
        return dispIP + " " + dispatcherName.elementAt(dispID);
    }

    public String getHostName(int i) throws RemoteException {
        return dispatcherIPAdrs.get(i);
    }

    public String getName(int i) throws RemoteException {
        return dispatcherName.get(i);
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

    /*
   generate the client id from the Client IP address
    */
    private static String clientIdGen(String addr) {
        String[] addrArray = addr.split("\\.");
        String temp = "";
        String value = "";
        for (int i = 0; i < addrArray.length; i++) {
            temp = addrArray[i];
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
            for (String[] ar : data) {
                dispatcherName.add(ar[0]);
                dispatcherIPAdrs.add(ar[1]);
                dispatcherPort.add(Integer.valueOf(ar[2]));
            }
        } catch (IOException e) {
        }
    }

    public void updateIncLoad(String ip) throws RemoteException {
        RBLoadBalancer.updateInc(ip);
    }

    public void updateDecLoad(String ip) throws RemoteException {
        RBLoadBalancer.updateDec(ip);
    }
}
