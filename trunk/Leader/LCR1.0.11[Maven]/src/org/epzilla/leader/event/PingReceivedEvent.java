package org.epzilla.leader.event;

public class PingReceivedEvent implements IEpzillaEvent {

	private String remoteIp;

	public PingReceivedEvent(String remoteIp) {
		this.remoteIp = remoteIp;
	}

	public String getData() {
		return remoteIp;
	}
}
