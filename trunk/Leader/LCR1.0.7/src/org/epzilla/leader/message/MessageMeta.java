package org.epzilla.leader.message;

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

}
