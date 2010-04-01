package org.epzilla.nameserver;

import org.epzilla.nameserver.loadbalance.RBLoadBalancer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

public class NameServiceImpl extends UnicastRemoteObject implements NameService {

    private int dirsize = 0;
    private static Vector<String> dispatcherName = new Vector<String>();
    private static Vector<String> dispatcherIPAdrs = new Vector<String>();
    private static Vector<Integer> dispatcherPort = new Vector<Integer>();

    public NameServiceImpl() throws RemoteException {
    }

    private int searchDisp(String s) {
        for (int i = 0; i < dirsize; i++)
            if (dispatcherIPAdrs.elementAt(i) == s)
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
            return 1;
        } else
            return 0;
    }


    //	    @Override
    public int getDirectorySize() throws RemoteException {
        return dirsize;
    }
    /*
   used for load balancer class
    */
//    public String getDispatcher(String clientID) throws RemoteException {
//        String dispIP = LoadBalancer.search(clientID, dirsize, ipAddrs);
//        int dispID = search(dispIP);
//        String toReturn = dispIP + " " + names[dispID];
//        return toReturn;
//    }

    public String getDispatcherIP() throws RemoteException {
        String dispIP = RBLoadBalancer.getIPAddress();
        int dispID = searchDisp(dispIP);
        String toReturn = dispIP + " " + dispatcherName.elementAt(dispID);
        return toReturn;
    }

    public void updateLoad(String ip) throws RemoteException {
        RBLoadBalancer.update(ip);
    }
}
