package org.epzilla.leader.rmi;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.epzilla.leader.EpzillaProcess;
import org.epzilla.leader.event.IEpzillaEventListner;
import org.epzilla.leader.message.Message;

public class LeaderMessageClient {

	/**
	 * @param args
	 * @throws NotBoundException
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws UnknownHostException
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws MalformedURLException,
			RemoteException, NotBoundException, UnknownHostException, InterruptedException {
		LeaderInterface li = (LeaderInterface) Naming
				.lookup("rmi://192.168.1.63/LeaderService");

		System.out.println("Via RMI : is Leader: " + li.isLeader());
		System.out.println("Via RMI : is Default Leader: "
				+ li.isDefaultLeader());
		
		li.receiveMessage(Message.getInstance().getUidMessage());
		
		Thread.currentThread();
		Thread.sleep(5000);

		li.electedLeader(Message.getInstance().getLeaderPublishMessage());

		System.out.println("Via RMI: is Leader: " + li.isLeader());
		System.out.println("Via RMI: get Status: " + li.getStatus());
		System.out.println("Via Static: get Cluster leader: "
				+ EpzillaProcess.getInstance().getClusterLeader());
		System.out.println("Via Static: get Status: "
				+ EpzillaProcess.getInstance().getStatus());

	}

	/**
	 * This method will return the search for the Leader interface and and return the Leader interface reference
	 * of a remote node
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
	public static void sendLeaderElectedMessage(String remoteIp)
			throws MalformedURLException, RemoteException, NotBoundException,
			UnknownHostException {
		LeaderInterface li = getLeaderInterface(remoteIp);

		li.electedLeader(Message.getInstance().getLeaderPublishMessage());
		// Local host declared as the Leader and message is sent
		// TODO remove
		System.out.println(li.getStatus());
		
		//TODO Remove
		System.out.println("Leader message published for this node since this is the Leader and sent to:"+remoteIp);

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
			String message) throws MalformedURLException, RemoteException,
			NotBoundException, UnknownHostException {
		LeaderInterface li = getLeaderInterface(remoteIp);

		li.receiveMessage(message);
		
		//TODO Remove
		System.out.println("Leader elected message forwarded since this node is in Non_Leader. Forwarded to:"+remoteIp);
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
			String receivedMessage) throws MalformedURLException,
			RemoteException, NotBoundException, UnknownHostException {

		LeaderInterface li = getLeaderInterface(remoteIp);

		li.receiveMessage(receivedMessage);

		System.out.println("The UID of higher order forwarded to:"+remoteIp);

	}

	/**
	 * This method is used to send the UID of this node if the received UID < self UID
	 * @param remoteIp
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 * @throws UnknownHostException
	 */
	public static void sendUidMessage(String remoteIp)
			throws MalformedURLException, RemoteException, NotBoundException,
			UnknownHostException {
		LeaderInterface li = getLeaderInterface(remoteIp);

		li.receiveMessage(Message.getInstance().getUidMessage());

		System.out.println("Recived UID is lower order. This UID sent to:"+remoteIp);
	}
	
	/**
	 * This method is used to send a pulse to non leader nodes in a timely manner.
	 * Will be used only by Leaders.
	 * @param remoteIp
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 * @throws UnknownHostException
	 */
	public static void sendPulse(String remoteIp) throws MalformedURLException, RemoteException, NotBoundException, UnknownHostException{
		LeaderInterface li=getLeaderInterface(remoteIp);
		
		li.receiveMessage(Message.getInstance().getPulse());
		
		System.out.println("Pulse sent to the non leader client:"+remoteIp);
	}
	
	/**
	 * This method is used to register the non leader epzilla event listeners with the Leader.
	 * Will only be used by Non Leader nodes. 
	 * @param remoteIp
	 * @param listener
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	public static void registerListenerWithLeader(String remoteIp,IEpzillaEventListner listener) throws RemoteException, MalformedURLException, NotBoundException{
		LeaderInterface li=getLeaderInterface(remoteIp);
		
		li.addListener(listener);
		
		System.out.println("Listner registered with the Leader and ready to listen to pulse.");
	}
	
	/**
	 * This method is used to unregister the non leader epzilla event listener with the leader.
	 * Will only be used by the Non leader nodes.
	 * @param remoteIp
	 * @param listener
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	public static void unregisterListenerWithLeader(String remoteIp,IEpzillaEventListner listener) throws RemoteException, MalformedURLException, NotBoundException{
		LeaderInterface li=getLeaderInterface(remoteIp);
		
		li.removeListener(listener);
		
		System.out.println("Listener unregistered from the leader.");
		
		//TODO Might not work since we have not overridden the hashcode() and equals() of IEpzillaEventListner implementation class. 
	}

}
