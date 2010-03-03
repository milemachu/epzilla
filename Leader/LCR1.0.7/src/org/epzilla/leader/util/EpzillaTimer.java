package org.epzilla.leader.util;

import java.util.TimerTask;

import org.epzilla.leader.EpzillaProcess;
import org.epzilla.leader.Status;
import org.epzilla.leader.event.PulseTimeoutEvent;

public class EpzillaTimer extends TimerTask {

	/**
	 * This will fire the pulse timeout event in the epzilla process.
	 */
	public void run() {
		if (EpzillaProcess.getInstance().getStatus().equalsIgnoreCase(
				Status.LEADER.toString()))
			EpzillaProcess.getInstance().fireEpzillaEvent(
					new PulseTimeoutEvent());
	}

}
