package org.epzilla.leader.rmi;

import java.net.UnknownHostException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import org.epzilla.leader.event.IEpzillaEventListner;

public interface LeaderInterface extends Remote {

	/**
	 * 
	 * @return the UID assigned to this machine by Administrator
	 * @throws RemoteException
	 */
	public int getUID() throws RemoteException;


	/**
	 * If some other node need to find out whether this node is the leader, this
	 * is called.
	 * 
	 * @return boolean whether the leader or not
	 * @throws RemoteException
	 */
	public boolean isLeader() throws RemoteException;

	/**
	 * This is to be called to get the status of the node
	 * 
	 * @return Status of the node
	 * @throws RemoteException
	 */
	public String getStatus() throws RemoteException;

	/**
	 * This is general method to pass the messages from host to host. This
	 * message will be decoded and will be passed to the Busi. Logic.
	 * 
	 * @param message
	 * @throws RemoteException
	 * @throws UnknownHostException
	 */
	public void receiveMessage(String message) throws RemoteException,
			UnknownHostException;

	/**
	 * There is a default leader declared for every STM based clusters. This
	 * method will tell whether this node is the default leader or not. Any node
	 * can invoke this method.
	 * 
	 * @throws RemoteException
	 */
	public boolean isDefaultLeader() throws RemoteException;

	/**
	 * This method is used to register the epzilla event listeners in leader for
	 * pulse,etc
	 * 
	 * @param listener
	 * @throws RemoteException
	 */
	public void addListener(IEpzillaEventListner listener)
			throws RemoteException;

	/**
	 * This method is used to unregister a given epzilla event listeners from
	 * the leader.
	 * 
	 * @param listener
	 * @throws RemoteException
	 */
	public void removeListener(IEpzillaEventListner listener)
			throws RemoteException;

	/**
	 * This is used to get the Leader address from a remote machine without
	 * running a Leader Election.
	 * 
	 * @return
	 * @throws RemoteException
	 */
	public String getClusterLeader() throws RemoteException;

	/**
	 * This method is used to identify the current state of the epzilla process
	 * with getStatus.
	 * <table>
	 * <table width="90%" border="0">
	 * <tr>
	 * <th scope="col">Stage</th>
	 * <th scope="col">isLeaderElectionRunning</th>
	 * <th scope="col">status</th>
	 * </tr>
	 * <tr>
	 * <td>Start up</td>
	 * <td>false</td>
	 * <td>unknown</td>
	 * </tr>
	 * <tr>
	 * <td>after receiving uid</td>
	 * <td>true</td>
	 * <td>unknown</td>
	 * </tr>
	 * <tr>
	 * <td>aftervreceiving leader</td>
	 * <td>false</td>
	 * <td>leader/nonleader</td>
	 * </tr>
	 * <tr>
	 * <td>after receiving a uid</td>
	 * <td>true</td>
	 * <td>unknown</td>
	 * </tr>
	 * </table>
	 * 
	 * @return
	 * @throws RemoteException
	 */
	public boolean isLeaderElectionRunning() throws RemoteException;

}
