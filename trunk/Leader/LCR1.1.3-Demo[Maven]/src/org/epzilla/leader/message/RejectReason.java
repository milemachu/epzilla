package org.epzilla.leader.message;

/**
 * This class is used to define the reasons for the message 6-NOT ACCEPTED HERE.
 * Reply Code from this class will be attached with the Type 6 reply to get the
 * requester to identify the rejected reason.
 * 
 * @author Administrator
 * 
 */
public class RejectReason {

	// This is the reject reason to reject the PING message.
	public static int NOT_ALLOWED_TO_RECEIVE_PING_NOT_LEADER = 400;
	// This is the reject reason to reject the PULSE message.
	public static int NOT_ALLOWED_TO_RECEIVE_PULSE_NOT_NON_LEADER = 401;
	// This is the reject reason to reject listener registration
	public static int NOT_ALLOWED_TO_REGISTER_LISTNER_NOT_LEADER = 402;

	public static String getErrorMessage(int errorCode) {
		String error = "Invalid Error Code";

		switch (errorCode) {
		case 400:
			error = "NOT_ALLOWED_TO_RECEIVE_PING_NOT_LEADER";
			break;
		case 401:
			error = "NOT_ALLOWED_TO_RECEIVE_PULSE_NOT_NON_LEADER";
			break;
		case 402:
			error = "NOT_ALLOWED_TO_REGISTER_LISTNER_NOT_LEADER";
			break;

		default:
			break;
		}

		return error;
	}

}
