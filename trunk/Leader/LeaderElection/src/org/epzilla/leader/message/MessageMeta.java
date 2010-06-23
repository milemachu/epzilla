package org.epzilla.leader.message;

/**
 * This interface defines the message IDs which are sent in RMI messages.
 * We use these IDS instead of message names to optimize the bandwidth by reducing the message content.
 * @author Administrator
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
