package org.epzilla.leader.event;

import java.util.TimerTask;

import org.epzilla.leader.EpzillaProcess;

public class EpzillaErrorStateExitTimer extends TimerTask {

	public void run() {
		// Fire Error occurred event
		// if(EpzillaProcess.getInstance().getStatus().equalsIgnoreCase(Status.NON_LEADER.toString())){
		EpzillaProcess.getInstance().fireEpzillaEvent(new ErrorOccurredEvent());
		// }

	}

}
