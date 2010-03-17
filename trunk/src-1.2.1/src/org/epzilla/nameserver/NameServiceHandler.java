package org.epzilla.nameserver;


import org.epzilla.dispatcher.rmi.*;

import java.net.InetAddress;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.io.IOException;

public class NameServiceHandler extends UnicastRemoteObject {
       Registry registry;
    public NameServiceHandler() throws RemoteException {
    }
    private void startRegistry(){
       Process rmiProcess = null;
        try{
            rmiProcess = Runtime.getRuntime().exec("rmiregistry");
            Thread.sleep(5000);
        }
        catch (IOException ex){
          //exception handling logic here
        }
        catch (InterruptedException exc){
           //exception handling logic here
        }
    }
   	public void bind(String serviceName){

		if(System.getSecurityManager()==null){
			System.setSecurityManager(new org.epzilla.dispatcher.rmi.OpenSecurityManager());
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
       	NameServiceHandler handler = new NameServiceHandler();
        handler.startRegistry();
    	handler.bind("NameServer");
    	
    }
	
	
}

