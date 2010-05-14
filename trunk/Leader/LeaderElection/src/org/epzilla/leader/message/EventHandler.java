package org.epzilla.leader.message;

import org.epzilla.leader.Epzilla;
import org.epzilla.leader.event.IEpzillaEvent;
import org.epzilla.leader.event.ProcessStatusChangedEvent;
import org.epzilla.leader.event.PulseIntervalTimeoutEvent;

public class EventHandler {
	
	public boolean fireEpzillaEvent(IEpzillaEvent event){
		if(event instanceof ProcessStatusChangedEvent){
			System.out.println("Process status changed to:"+Epzilla.getStatus());
			Epzilla.resetTimerQueue();
		}else if(event instanceof PulseIntervalTimeoutEvent){
			if(Epzilla.getComponentType().equalsIgnoreCase(anotherString))
		}
		
		
		return false;
	}
}
