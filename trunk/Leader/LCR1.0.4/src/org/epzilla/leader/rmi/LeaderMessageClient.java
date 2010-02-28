package org.epzilla.leader.rmi;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.epzilla.leader.EpzillaProcess;
import org.epzilla.leader.message.Message;

public class LeaderMessageClient {

	/**
	 * @param args
	 * @throws NotBoundException 
	 * @throws RemoteException 
	 * @throws MalformedURLException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException, UnknownHostException {
		LeaderInterface li=(LeaderInterface)Naming.lookup("rmi://127.0.0.1/LeaderService");
		
		System.out.println("Via RMI : is Leader: "+li.isLeader());
		System.out.println("Via RMI : is Default Leader: "+li.isDefaultLeader());
		
		li.electedLeader(Message.getInstance().getLeaderPublishMessage());
		
		System.out.println("Via RMI: is Leader: "+li.isLeader());
		System.out.println("Via RMI: get Status: "+li.getStatus());
		System.out.println("Via Static: get Cluster leader: "+EpzillaProcess.getInstance().getClusterLeader());
		System.out.println("Via Static: get Status: "+EpzillaProcess.getInstance().getStatus());
		
		

	}

}
