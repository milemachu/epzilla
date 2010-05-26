package org.epzilla.leader.event;

public class RequestRejectedEvent implements IEpzillaEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -360260003820301729L;
	private int errorCode;

	public RequestRejectedEvent(int errorCode) {
		this.errorCode = errorCode;
	}

	public int getData() {
		return errorCode;
	}
}
