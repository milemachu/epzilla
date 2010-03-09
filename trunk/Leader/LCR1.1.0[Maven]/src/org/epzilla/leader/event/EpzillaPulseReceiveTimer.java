package org.epzilla.leader.event;

import java.util.TimerTask;

import org.epzilla.leader.EpzillaProcess;
import org.epzilla.leader.util.Status;

public class EpzillaPulseReceiveTimer extends TimerTask{

	/**
	 * This will fire the pulse not received event.
	 */
	public void run() {
		// Fire pulse not received.
		if(EpzillaProcess.getInstance().getStatus().equalsIgnoreCase(Status.NON_LEADER.toString())){
			EpzillaProcess.getInstance().fireEpzillaEvent(new PulseNotReceivedTimeoutEvent());
		}
	}

}
