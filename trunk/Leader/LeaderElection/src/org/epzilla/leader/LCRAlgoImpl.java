package org.epzilla.leader;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.epzilla.leader.message.MessageMeta;
import org.epzilla.leader.util.Status;

/**
 * This class contains the LCR leader election algorithm implementation. All the
 * necessary LCR specific methods are here. Based on the assumption that 1 is
 * the highest priority in UID sequence.
 * 
 * @author Administrator
 * 
 */
public class LCRAlgoImpl {

	

	// This is the LCR logic
	public String runAlgorithm(String message) {
		String[] strArray = message.split(Character
				.toString(MessageMeta.SEPARATOR));

		if (strArray != null && Integer.parseInt(strArray[1]) >= 1) {
			int receivedUid = Integer.parseInt(strArray[1]);
			if (receivedUid == Epzilla.getUID()) {
				Epzilla.setClusterLeader(strArray[2]);
				Epzilla.setStatus(Status.LEADER.toString());
//					Epzilla.fireEpzillaEvent(
//							new ProcessStatusChangedEvent());
				Epzilla.setLeaderElectionRunning(false);
					
					return Status.LEADER.name();
			
				} else if (receivedUid < Epzilla.getUID()) {
				// Pass the message to next node
				// If we want we can declare this as a non leader since this is
				// going to be declared as a non leader upon the receiving the
				// Leader published message.

//				try {
//					// remove hardcode and get from the ip list
//					LeaderMessageClient.forwardReceivedUidMessage(
//							EpzillaProcess.getInstance().getIpNextToThis(),
//							message);
//				} catch (MalformedURLException e) {
//
//					e.printStackTrace();
//				} catch (RemoteException e) {
//
//					e.printStackTrace();
//				} catch (UnknownHostException e) {
//
//					e.printStackTrace();
//				} catch (NotBoundException e) {
//
//					e.printStackTrace();
//				}
					return Status.UNKNOWN.name();
			} else if (receivedUid > Epzilla.getUID()) {
				// No Forwarding of received message.Instead start sending the
				// UID of this
				// Remove the hardcode and get it from the list
//				try {
//					LeaderMessageClient.sendUidMessage(EpzillaProcess
//							.getInstance().getIpNextToThis());
//				} catch (MalformedURLException e) {
//
//					e.printStackTrace();
//				} catch (RemoteException e) {
//
//					e.printStackTrace();
//				} catch (UnknownHostException e) {
//
//					e.printStackTrace();
//				} catch (NotBoundException e) {
//
//					e.printStackTrace();
//				}
				
				return Status.UNKNOWN.name();
			}
		}
	
return null;
	
	}
}
