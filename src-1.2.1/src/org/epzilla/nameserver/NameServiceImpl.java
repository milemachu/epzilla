package org.epzilla.nameserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class NameServiceImpl extends UnicastRemoteObject implements NameService {

        final static int maxSize = 100;
	    private  static String[] names = new String[maxSize];
	    private  static String[] ipAddrs = new String[maxSize];
	    private  static int[] ports = new int[maxSize];
	    private int dirsize = 0;
        ArrayList<String> ipArr = new ArrayList<String>();
	    
	    public NameServiceImpl()throws RemoteException{}

	    public int search(String str) throws RemoteException {
	        for (int i = 0; i < dirsize; i++)
	            if (ipAddrs[i].equals(str)) return i;
	        return -1;
	    }
	    public int insertNode(String name, String ipAdrs, int portNumber)
	            throws RemoteException {
	    	int oldIndex = search(ipAdrs); // is it already there
	        if ((oldIndex == -1) && (dirsize < maxSize)) { 
	            names[dirsize] = name;
	            ipAddrs[dirsize] = ipAdrs;
	            ports[dirsize] = portNumber;
	            dirsize++;
	            return 1;
	        } else
	            return 0;
	    }
//	    @Override
	    public int getPort(int index) throws RemoteException {
	        return ports[index];
	    }
	  //  @Override
	    public String getHostName(int index) throws RemoteException {
	        return ipAddrs[index];
	    }
	    //@Override
		public String getNames(int index) throws RemoteException {
	    	return names[index];
		}
//	    @Override
		public int getDirectorySize() throws RemoteException {
	    	return dirsize;
		}
        public int getDispatcherID() throws RemoteException {
            LoadBalancer lBalance = new LoadBalancer();
            int dispID=0;
            for(int i=0;i<dirsize;i++){
                ipArr.add(ipAddrs[i]);
            }
              dispID = lBalance.selectRandIP(ipArr);
              return dispID;
        }

}
