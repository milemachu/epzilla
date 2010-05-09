package org.epzilla.leader.event;

public class PingReceivedEvent implements IEpzillaEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7142169833744394727L;
	private String remoteIp;

	public PingReceivedEvent(String remoteIp) {
		this.remoteIp = remoteIp;
	}

	public String getData() {
		return remoteIp;
	}
}
