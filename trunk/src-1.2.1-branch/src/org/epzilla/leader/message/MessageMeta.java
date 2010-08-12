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
 * This interface defines the message IDs which are sent in RMI messages.
 * We use these IDS instead of message names to optimize the bandwidth by reducing the message content.
 * @author Harshana Eranga Martin
 *
 */
public interface MessageMeta {
	public static char SEPARATOR = '\u0016';

	// This is to send the UID for leader election
	public static byte UID = 1;
	// This is the message id of the leader announcement
	public static byte LEADER = 2;
	// This is the message id of the non leaders
	public static byte NON_LEADER = 3;
	// This is the pulse of the Leader to send to others
	public static byte PULSE = 4;
	// This is the Leader ping message
	public static byte PING_LEADER = 5;
	// If the request cannot be completed here, send this.
	public static byte REQUEST_NOT_ACCEPTED = 6;

}
