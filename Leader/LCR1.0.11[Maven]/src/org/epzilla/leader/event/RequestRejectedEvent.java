package org.epzilla.leader.event;

public class RequestRejectedEvent implements IEpzillaEvent {

	private int errorCode;
	
	public RequestRejectedEvent(int errorCode) {
	this.errorCode=errorCode;
	}
	
	public int getData(){
		return errorCode;
	}
}
