package org.epzilla.leader.event;

public class ListenerRegisteredEvent implements IEpzillaEvent {
	private String remoteIp;

	public ListenerRegisteredEvent(String remoteIp) {

		this.remoteIp = remoteIp;
	}

	public String getData() {
		return remoteIp;
	}
}
