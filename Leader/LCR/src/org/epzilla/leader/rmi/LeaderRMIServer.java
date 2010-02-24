package org.epzilla.leader.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

public class LeaderRMIServer {

	/**
	 * @param args
	 * @throws RemoteException 
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws RemoteException, MalformedURLException {
				
		if(System.getSecurityManager()==null){
			System.setSecurityManager(new RMISecurityManager());
		}
		
		LeaderInterface impl=new LeaderImpl();
		
		Naming.rebind("rmi://127.0.0.1/LeaderService", impl);
		
		System.out.println("Leader election service successfully deployed and running.");

	}

}
