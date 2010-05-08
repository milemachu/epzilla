package org.epzilla.leader.message;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.epzilla.leader.EpzillaProcess;
import org.epzilla.leader.LCRAlgoImpl;
import org.epzilla.leader.event.EpZillaListener;
import org.epzilla.leader.event.PingReceivedEvent;
import org.epzilla.leader.event.ProcessStatusChangedEvent;
import org.epzilla.leader.event.PulseIntervalTimeoutEvent;
import org.epzilla.leader.event.PulseReceivedEvent;
import org.epzilla.leader.event.RequestRejectedEvent;
import org.epzilla.leader.rmi.LeaderMessageClient;
import org.epzilla.leader.util.Status;

/**
 * This class will decode the incoming message from the others to RMI server of
 * local host. It will send the result to the Business Logic of the algorithm
 * and do the work.
 * 
 * @author Administrator
 * 
 */
public class MessageDecoder implements Runnable {
	EpzillaProcess coreInstance;
	String message;

	public MessageDecoder(String message) {
		coreInstance = EpzillaProcess.getInstance();
		this.message = message;
	}

	// This method will decode the message
	public void decodeMessage() throws UnknownHostException {
		String[] strItems = message.split(Character
				.toString(MessageMeta.SEPARATOR));

		String messageType = Message.getInstance().getMessage(
				Integer.parseInt(strItems[0]));
		System.out.println("New Message Received: " + messageType);

		// Logic here to pass the message type with parameters to B.L.

		// Elected leader send message
		if (Integer.parseInt(strItems[0]) == MessageMeta.LEADER) {
			// This message is from Leader and to Leader publish msg and
			// received at electedLeader.

			if (strItems[1].equalsIgnoreCase(InetAddress.getLocalHost()
					.getHostAddress())) {

				System.out.println("Leader is the Localhost");
				// No need to forward the LEADER message. Do nothing now about
				// that message.
				// Set the timer for Pulse
				coreInstance.fireEpzillaEvent(new PulseIntervalTimeoutEvent());
			} else {
				coreInstance.setClusterLeader(InetAddress
						.getByName(strItems[1]));
				coreInstance.setStatus(Status.NON_LEADER.toString());
				coreInstance.fireEpzillaEvent(new ProcessStatusChangedEvent());
				coreInstance.setLeaderElectionRunning(false);
				System.out
						.println("Leader is else and this is  a NON leader. Status changed event fired.");

				// Forward the Leader message to next host and register this
				// node as a listener.
				try {					
					//TODO:What if the next ip is not alive.
					LeaderMessageClient.forwardLeaderElectedMessage(
							EpzillaProcess.getInstance().getIpNextToThis(),
							message);
					LeaderMessageClient.registerListenerWithLeader(strItems[1],
							new EpZillaListener());

					// Set the pulse receive timer here for the first time
					// cannot remove since we register the listener and server
					// supposed to send a pulse for real.
					// But it is possible that 1st pulse also not receiving.
					EpzillaProcess.getInstance().fireEpzillaEvent(
							new PulseReceivedEvent(strItems[1]));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotBoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			System.out
					.println("Leader published and updated at the local instance.");
			System.out.println("Leader is "
					+ EpzillaProcess.getInstance().getClusterLeader());
			System.out.println("Local host is " + InetAddress.getLocalHost());
			System.out.println("Local host Status:"
					+ EpzillaProcess.getInstance().getStatus());

			// //Now leader election complete. So no more election is running.
			// EpzillaProcess.getInstance().setLeaderElectionRunning(false);

			// Now create a Thread to count down for the pulse
			// When PULSE received reset the count down
		} else if (Integer.parseInt(strItems[0]) == MessageMeta.UID) {
			// Initiate the Leader Election and Set the status to unknown
			// Init the cluster leader
			EpzillaProcess.getInstance().setClusterLeader(null);
			EpzillaProcess.getInstance().setStatus(Status.UNKNOWN.toString());
			EpzillaProcess.getInstance().setLeaderElectionRunning(true);
			// Fire status changed event
			EpzillaProcess.getInstance().fireEpzillaEvent(
					new ProcessStatusChangedEvent());
			// Forward the received UID to LCR and let it handle it.
			LCRAlgoImpl.getInstance().runAlgorithm(message);

		} else if (Integer.parseInt(strItems[0]) == MessageMeta.PULSE) {
			// TODO TEST
			System.out
					.println("PULSE Recieved from the Leader and the Pulse Received event fired.");
			// Firing the pulse received event
			EpzillaProcess.getInstance().fireEpzillaEvent(
					new PulseReceivedEvent(strItems[1]));

		} else if (Integer.parseInt(strItems[0]) == MessageMeta.PING_LEADER) {
			System.out
					.println("PING received to this node and Ping Received event fired.");
			// fire the ping received event. It will handle the logic of sending
			// PULSE or REQUEST NOT ACCEPTED
			EpzillaProcess.getInstance().fireEpzillaEvent(
					new PingReceivedEvent(strItems[1]));
		} else if (Integer.parseInt(strItems[0]) == MessageMeta.REQUEST_NOT_ACCEPTED) {
			System.out.println("REQUEST_NOT_ACCEPTED received from:"
					+ strItems[1]);
			// Whatever the last request to this host is not valid.Try to track the reason via message sequence from this
			// side+response from other side.Not implemented yet.

			System.out.println("Request rejected due to:"
					+ RejectReason.getErrorMessage(Integer
							.parseInt(strItems[2])));

			EpzillaProcess.getInstance().fireEpzillaEvent(
					new RequestRejectedEvent(Integer.parseInt(strItems[2])));

		}

	}

	public void run() {
		try {
			decodeMessage();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
