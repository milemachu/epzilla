package org.epzilla.leader.event;

public class PulseReceivedEvent implements IEpzillaEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3621156355194099361L;
	private String remoteIp;

	public PulseReceivedEvent(String remoteIp) {
		this.remoteIp = remoteIp;
	}

	public String getData() {
		return remoteIp;
	}
}
