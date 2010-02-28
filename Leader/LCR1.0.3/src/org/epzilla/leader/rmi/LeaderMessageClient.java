package org.epzilla.leader.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class LeaderMessageClient {

	/**
	 * @param args
	 * @throws NotBoundException 
	 * @throws RemoteException 
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		LeaderInterface li=(LeaderInterface)Naming.lookup("rmi://127.0.0.1/LeaderService");
		
		System.out.println(
		li.isLeader());

	}

}
