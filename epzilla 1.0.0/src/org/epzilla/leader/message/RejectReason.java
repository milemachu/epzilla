/***************************************************************************************************
Copyright 2009 - 2010 Harshana Eranga Martin, Dishan Metihakwala, Rajeev Sampath, Chathura Randika

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
****************************************************************************************************/
package org.epzilla.leader.message;

/**
 * This class is used to define the reasons for the message 6-NOT ACCEPTED HERE.
 * Reply Code from this class will be attached with the Type 6 reply to get the
 * requester to identify the rejected reason.
 * 
 * @author Harshana Eranga Martin 	 mailto: harshana05@gmail.com
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
