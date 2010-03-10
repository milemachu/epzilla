package org.epzilla.leader.rmi;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

public class LeaderRMIServer {

	/**
	 * @param args
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws UnknownHostException
	 */
	public static void main(String[] args) throws RemoteException,
			MalformedURLException, UnknownHostException {

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
		}

		LeaderInterface impl = new LeaderImpl();

		Naming.rebind("rmi://" + InetAddress.getLocalHost().getHostAddress()
				+ "/LeaderService", impl);

		System.out
				.println("Leader election service successfully deployed and running.");

	}

	public static boolean deployLeaderService() {
		try {
			if (System.getSecurityManager() == null) {
				System.setSecurityManager(new RMISecurityManager());
			}

			LeaderInterface impl = new LeaderImpl();

			Naming.rebind("rmi://"
					+ InetAddress.getLocalHost().getHostAddress()
					+ "/LeaderService", impl);

			System.out
					.println("Leader election service successfully deployed and running.");

			return true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

}
