package org.epzilla.daemon.services;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class DaemonWakeCaller {
	private static String SERVICE_NAME="DAEMON_SERVICE";
	
	public boolean callWake(String remoteIp){
		try {
			DaemonInterface di=(DaemonInterface) Naming.lookup("rmi://"+remoteIp+"/"+SERVICE_NAME);
			return(di.wakeEpZilla());			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}	
		return false;
	}
	/**
	 * @param args
	 * @throws NotBoundException 
	 * @throws RemoteException 
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		// TODO Auto-generated method stub
		DaemonInterface di = (DaemonInterface) Naming.lookup("rmi://127.0.0.1/DAEMON_SERVICE");
		di.wakeEpZilla();
	}

}
