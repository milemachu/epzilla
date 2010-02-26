package org.epzilla.nameserver;


import java.net.InetAddress;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class NameServiceHandler extends UnicastRemoteObject implements NameService {
    final static int maxSize = 100;
    private  static String[] names = new String[maxSize];
    private  static String[] ipAddrs = new String[maxSize];
    private  static int[] ports = new int[maxSize];
    private int dirsize = 0;
    public NameServiceHandler() throws RemoteException {
    }
    public int search(String str) throws RemoteException {
        for (int i = 0; i < dirsize; i++)
            if (names[i].equals(str)) return i;
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
        } else // already there, or table full
            return 0;
    }
//    @Override
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
//    @Override
	public int getDirectorySize() throws RemoteException {
    	return dirsize;
	}
	public void bind(String serviceName){
		try {
        	InetAddress inetAddress = InetAddress.getLocalHost();
        	String ipAddress = inetAddress.getHostAddress();
        	String url ="rmi://"+ipAddress+"/"+serviceName;
        	//String url = "rmi://127.0.0.1/NameServer";
            NameServiceHandler obj = new NameServiceHandler();
            Naming.rebind(url, obj);

            System.out.println("NameServer bound in registry");
        } catch (Exception e) {
            System.out.println("NameService err: " + e.getMessage());
        }

	}
    public static void main(String args[]) throws RemoteException {
        //System.setSecurityManager(new RMISecurityManager());
    	NameServiceHandler handler = new NameServiceHandler();
    	handler.bind("NameServer");
    	
    }
	
	
}

