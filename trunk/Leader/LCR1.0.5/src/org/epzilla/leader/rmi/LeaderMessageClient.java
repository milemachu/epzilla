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
	
	private static LeaderInterface getLeaderInterface(String remoteIp) throws MalformedURLException, RemoteException, NotBoundException{
		
		LeaderInterface li=(LeaderInterface)Naming.lookup("rmi://"+remoteIp+"/LeaderService");
		
		return li;
	}
	
	/**
	 * This is the method used to send the Leader publish message
	 * @param remoteIp
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 * @throws UnknownHostException
	 */
	public static void sendLeaderElectedMessage(String remoteIp) throws MalformedURLException, RemoteException, NotBoundException, UnknownHostException{
		LeaderInterface li=getLeaderInterface(remoteIp);
		
		li.electedLeader(Message.getInstance().getLeaderPublishMessage());
		//Local host declared as the Leader and message is sent
		//TODO remove
		System.out.println(li.getStatus());
		
	}
	
	/**
	 * This method is used to forward the leader published message
	 * @param remoteIp
	 * @param message
	 * @throws NotBoundException 
	 * @throws RemoteException 
	 * @throws MalformedURLException 
	 * @throws UnknownHostException 
	 */
	public static void forwardLeaderElectedMessage(String remoteIp,String message) throws MalformedURLException, RemoteException, NotBoundException, UnknownHostException{
		LeaderInterface li=getLeaderInterface(remoteIp);
		
		li.receiveMessage(message);
	}
	
	/**
	 * This is the method used to send the LCR algo message when the message uid is higher than self 
	 * @param remoteIp
	 * @param receivedMessage
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 * @throws UnknownHostException
	 */
	public static void forwardReceivedUidMessage(String remoteIp,String receivedMessage) throws MalformedURLException, RemoteException, NotBoundException, UnknownHostException{
		
		LeaderInterface li=getLeaderInterface(remoteIp);
		
		li.receiveMessage(receivedMessage);
		
		System.out.println("The UID of higher order forwarded.");
		
	}
	
	public static void sendUidMessage(String remoteIp) throws MalformedURLException, RemoteException, NotBoundException, UnknownHostException{
		LeaderInterface li=getLeaderInterface(remoteIp);
		
		li.receiveMessage(Message.getInstance().getUidMessage());
		
		System.out.println("Recived UID is lower order. This UID sent.");
	}

}
