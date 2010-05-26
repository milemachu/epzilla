package org.epzilla.leader.event;

public class ListenerRegisteredEvent implements IEpzillaEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5704074913722250768L;
	private String remoteIp;

	public ListenerRegisteredEvent(String remoteIp) {

		this.remoteIp = remoteIp;
	}

	public String getData() {
		return remoteIp;
	}
}
