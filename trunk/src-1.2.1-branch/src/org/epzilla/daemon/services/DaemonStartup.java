package org.epzilla.daemon.services;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

public class DaemonStartup {
	private static String SERVICE_NAME="DAEMON_SERVICE";
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args){
		if(System.getSecurityManager()==null){
			System.setSecurityManager(new RMISecurityManager());
		}
		DaemonInterface daemon;
		try {
			daemon = new DaemonImpl();
			InetAddress inetAddress;
			inetAddress = InetAddress.getLocalHost();
	    	String ipAddress = inetAddress.getHostAddress();
	    	String name ="rmi://"+ipAddress+"/"+SERVICE_NAME;
			Naming.rebind(name, daemon);
			System.out.println("Daemon Service successfully deployed.....");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}	
		
	}

}
