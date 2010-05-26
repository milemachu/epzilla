package org.epzilla.leader.message;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


import org.epzilla.leader.event.listner.IEpzillaEventListner;
import org.epzilla.leader.rmi.LeaderInterface;

public class RmiMessageClient {

	/**
	 * This method will return the search for the Leader interface and and
	 * return the Leader interface reference of a remote node
	 * 
	 * @param remoteIp
	 * @return
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	private static LeaderInterface getLeaderInterface(String remoteIp)
			throws MalformedURLException, RemoteException, NotBoundException {

		LeaderInterface li = (LeaderInterface) Naming.lookup("rmi://"
				+ remoteIp + "/LeaderService");

		return li;
	}

	/**
	 * This is the method used to send the Leader publish message
	 * 
	 * @param remoteIp
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 * @throws UnknownHostException
	 */
	public static void sendLeaderElectedMessage(String remoteIp){
		LeaderInterface li;
		try {
			li = getLeaderInterface(remoteIp);
			li.receiveMessage(MessageGenerator.getLeaderPublishMessage());
		} catch (Exception e) {
			System.err.println(e.getCause());
		}
	}

	/**
	 * This method is used to forward the leader published message
	 * 
	 * @param remoteIp
	 * @param message
	 * @throws NotBoundException
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws UnknownHostException
	 */
	public static void forwardLeaderElectedMessage(String remoteIp,
			String message){
		LeaderInterface li;
		try {
			li = getLeaderInterface(remoteIp);
			li.receiveMessage(message);
		} catch (Exception e) {
			System.err.println(e.getCause());
		}
	}

	/**
	 * This is the method used to send the LCR algo message when the message uid
	 * is higher than self
	 * 
	 * @param remoteIp
	 * @param receivedMessage
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 * @throws UnknownHostException
	 */
	public static void forwardReceivedUidMessage(String remoteIp,
			String receivedMessage){

		LeaderInterface li;
		try {
			li = getLeaderInterface(remoteIp);
			System.out.println("The UID of lower order forwarded to:" + remoteIp);
			li.receiveMessage(receivedMessage);
		} catch (Exception e) {
			System.err.println(e.getCause());
		}
		
	}

	/**
	 * This method is used to send the UID of this node if the received UID <
	 * self UID
	 * 
	 * @param remoteIp
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 * @throws UnknownHostException
	 */
	public static void sendUidMessage(String remoteIp){
		LeaderInterface li;
		try {
			li = getLeaderInterface(remoteIp);
			System.out.println("Recived UID is higher or same order. This UID sent to:"+ remoteIp);
			li.receiveMessage(MessageGenerator.getUidMessage());
		} catch (Exception e) {
			System.err.println(e.getCause());
		}
		
	}

	/**
	 * This method is used to send a pulse to non leader nodes in a timely
	 * manner. Will be used only by Leaders.
	 * 
	 * @param remoteIp
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 * @throws UnknownHostException
	 */
	public static void sendPulse(String remoteIp){
		LeaderInterface li;
		try {
			li = getLeaderInterface(remoteIp);
			System.out.println("Pulse sent to the non leader client:" + remoteIp);
			li.receiveMessage(MessageGenerator.getPulse());
		} catch (Exception e) {
			System.err.println(e.getCause());
		}
	}

	/**
	 * This method is used to register the non leader epzilla event listeners
	 * with the Leader. Will only be used by Non Leader nodes.
	 * 
	 * @param remoteIp
	 * @param listener
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	public static void registerListenerWithLeader(String remoteIp,
			IEpzillaEventListner listener)  {
		LeaderInterface li;
		try {
			li = getLeaderInterface(remoteIp);
			li.addListener(listener);
			System.out
			.println("Listner registered with the Leader "+remoteIp+" and ready to listen to pulse.");
		} catch (Exception e) {
			System.err.println(e.getCause());
		}	
	}

	/**
	 * This method is used to unregister the non leader epzilla event listener
	 * with the leader. Will only be used by the Non leader nodes.
	 * 
	 * @param remoteIp
	 * @param listener
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	public static void unregisterListenerWithLeader(String remoteIp,
			IEpzillaEventListner listener){
		LeaderInterface li;
		try {
			li = getLeaderInterface(remoteIp);
			li.removeListener(listener);
		} catch (Exception e) {
			System.err.println(e.getCause());
		}
	}

	/**
	 * If s host is unable to accepted a message to not authorized to reply,
	 * this message is sent.
	 * 
	 * @param remoteIp
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 * @throws UnknownHostException
	 */
	public static void sendRequestNotAccepted(String remoteIp, int errorCode){
		LeaderInterface li;
		try {
			li = getLeaderInterface(remoteIp);			
			li.receiveMessage(MessageGenerator.getRequestNotAccepted()+ errorCode + MessageMeta.SEPARATOR);
			System.out.println("Request not accepted sent to:" + remoteIp);
		}  catch (Exception e) {
			System.err.println(e.getCause());
		}
	}

	/**
	 * This is used to send Ping message to a given node.
	 * 
	 * @param remoteIp
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 * @throws UnknownHostException
	 */
	public static void sendPing(String remoteIp){
		LeaderInterface li;
		try {
			li = getLeaderInterface(remoteIp);
			li.receiveMessage(MessageGenerator.getPing());
			System.out.println("Ping sent to:" + remoteIp);
		} catch (Exception e) {
			System.err.println(e.getCause());
		}
	}

	/**
	 * This is used to get the Leader address from a remote machine without
	 * running a Leader Election.
	 * 
	 * @param remoteIp
	 * @return
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 * @throws UnknownHostException
	 */
	public static String getClusterLeaderFromRemote(String remoteIp){
		LeaderInterface li;
		try {
			li = getLeaderInterface(remoteIp);
			System.out.println("get cluster leader sent to:" + remoteIp);
			return li.getClusterLeader();
		} catch (Exception e) {
			System.err.println(e.getCause());
		}

		return null;
	}

	/**
	 * This method is used to get the status of a remote machine.
	 * 
	 * @param remoteIp
	 * @return
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public static String getStateFromRemote(String remoteIp){
		LeaderInterface li;
		try {
			li = getLeaderInterface(remoteIp);
			System.out.println("get cluster leader sent to:" + remoteIp);
			return li.getStatus();
		} catch (Exception e) {
			System.err.println(e.getCause());
		}	
		return null;		
	}

	/**
	 * This is used to check whether the LE is running in a remote host.
	 * 
	 * @param remoteIp
	 * @return
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public static boolean isLeaderElectioRunningInRemote(String remoteIp){
		LeaderInterface li;
		try {
			li = getLeaderInterface(remoteIp);
			System.out.println("get is Leader running in remote sent to:"+ remoteIp);
			return li.isLeaderElectionRunning();
		} catch (Exception e) {
			System.err.println(e.getCause());
		}
		return false;		
	}

}
