package org.epzilla.nameserver;

import org.epzilla.nameserver.xml.ServerSettingsReader;
import org.epzilla.nameserver.loadbalance.LoadBalancer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.io.IOException;

public class NameServiceImpl extends UnicastRemoteObject implements NameService {

        static int maxSize = 100;
	    private  static String[] names = new String[maxSize];
	    private  static String[] ipAddrs = new String[maxSize];
	    private  static int[] ports = new int[maxSize];
	    private int dirsize = 0;
        ArrayList<String> ipArr = new ArrayList<String>();
        static ServerSettingsReader reader = new ServerSettingsReader();

	    
	    public NameServiceImpl()throws RemoteException{
        }

	    public int search(String str) throws RemoteException {
	        for (int i = 0; i < dirsize; i++)
	            if (ipAddrs[i].equals(str))
                    return i;
	        return -1;
	    }
	    public int insertNode(String name, String ipAdrs, int portNumber)throws RemoteException {
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
        public String getDispatcher(String clientID) throws RemoteException {
            String dispIP = LoadBalancer.search(clientID,dirsize,ipAddrs);
            int dispID = search(dispIP);
            String toReturn = dispIP+" "+names[dispID];
            return toReturn;
        }
    /*
    load maximum number of Dispatchers from the XML file
    currently not included
     */
     private void loadSettings(){
			try {
				ArrayList<String[]> data = reader.getServerIPSettings("./src/org/epzilla/nameserver/nameserver_settings.xml");
				String[] ar = data.get(0);
				maxSize=Integer.parseInt(ar[0]);
                } catch (IOException e) {
				e.printStackTrace();
			}
		}

}
