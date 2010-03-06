package org.epzilla.nameserver;


import java.net.InetAddress;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class NameServiceHandler extends UnicastRemoteObject {
       Registry registry;
    public NameServiceHandler() throws RemoteException {
    }
   	public void bind(String serviceName){
		if(System.getSecurityManager()==null){
			System.setSecurityManager(new RMISecurityManager());
		}
		try {
        	InetAddress inetAddress = InetAddress.getLocalHost();
        	String ipAddress = inetAddress.getHostAddress();
        	String url ="rmi://"+ipAddress+"/"+serviceName;
        	NameService obj = new NameServiceImpl();
            Naming.rebind(url, obj);
            System.out.println("NameServer successfully deployed");
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

