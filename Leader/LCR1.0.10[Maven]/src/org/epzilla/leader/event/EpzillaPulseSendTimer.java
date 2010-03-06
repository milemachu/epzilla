package org.epzilla.leader.event;

import java.util.TimerTask;

import org.epzilla.leader.EpzillaProcess;
import org.epzilla.leader.Status;

public class EpzillaPulseSendTimer extends TimerTask {

	/**
	 * This will fire the pulse timeout event in the epzilla process.
	 */
	public void run() {
		if (EpzillaProcess.getInstance().getStatus().equalsIgnoreCase(
				Status.LEADER.toString()))
			EpzillaProcess.getInstance().fireEpzillaEvent(
					new PulseIntervalTimeoutEvent());
	}

}
