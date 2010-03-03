package org.epzilla.leader;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.epzilla.leader.message.MessageMeta;
import org.epzilla.leader.rmi.LeaderMessageClient;

/**
 * This class contains the LCR leader election algorithm implementation. All the
 * necessary LCR specific methods are here.
 * 
 * @author Administrator
 * 
 */
public class LCRAlgoImpl {

	// Private constructor prevents instantiation from other classes
	private LCRAlgoImpl() {
	}

	/**
	 * SingletonHolder is loaded on the first execution of
	 * Singleton.getInstance() or the first access to SingletonHolder.INSTANCE,
	 * not before.
	 */
	private static class LCRAlgoImplHolder {
		private static final LCRAlgoImpl INSTANCE = new LCRAlgoImpl();
	}

	public static LCRAlgoImpl getInstance() {
		return LCRAlgoImplHolder.INSTANCE;
	}

	// This is the LCR logic
	public void runAlgorithm(String message) {
		String[] strArray = message.split(Character
				.toString(MessageMeta.SEPARATOR));

		if (strArray != null && Integer.parseInt(strArray[1]) >= 1) {
			int receivedUid = Integer.parseInt(strArray[1]);
			if (receivedUid == EpzillaProcess.UID) {
				// This is the Leader. Send the Leader message to others
				// UPDATE the local variables such as ClusterLeader and Status

				try {
					// TODO remove the hardcode address and get it from the ip
					// list
					LeaderMessageClient
							.sendLeaderElectedMessage("192.168.1.63");
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotBoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (receivedUid > EpzillaProcess.UID) {
				// Pass the message to next node
				try {
					// TODO remove hardcode and get from the ip list
					LeaderMessageClient.forwardReceivedUidMessage(
							"192.168.1.63", message);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotBoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (receivedUid < EpzillaProcess.UID) {
				// No Forwarding of received message.Instead start sending the
				// UID of this
				// TODO Remove the hardcode and get it from the list
				try {
					LeaderMessageClient.sendUidMessage("192.168.1.63");
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotBoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

}
