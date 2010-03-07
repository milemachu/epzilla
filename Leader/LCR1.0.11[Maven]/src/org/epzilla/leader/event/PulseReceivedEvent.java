package org.epzilla.leader.event;

public class PulseReceivedEvent implements IEpzillaEvent {
	private String remoteIp;
	
	public PulseReceivedEvent(String remoteIp) {
		this.remoteIp=remoteIp;
	}
	
	public String getData(){
		return remoteIp;
	}
}
