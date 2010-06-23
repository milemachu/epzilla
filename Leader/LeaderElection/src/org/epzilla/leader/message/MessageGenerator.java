package org.epzilla.leader.message;

import java.net.InetAddress;

import org.epzilla.leader.Epzilla;

/**
 * This is the class used to generate messages to send using RMI to other nodes.
 * @author Administrator
 *
 */
public class MessageGenerator {
	
	/**
	 * MESSAGEId Separator || Separator IP Separator Status Separator
	 */

	public static String getUidMessage() {
		/**
		 * MESSAGEId Separator |UID| Separator IP Separator Status Separator
		 */
		StringBuilder sb = new StringBuilder();
		try {
			sb.append(MessageMeta.UID).append(MessageMeta.SEPARATOR).append(Epzilla.getUID()).append(
			MessageMeta.SEPARATOR).append(InetAddress.getLocalHost().getHostAddress()).append(
			MessageMeta.SEPARATOR).append(Epzilla.getStatus()).append(MessageMeta.SEPARATOR);
		} catch (Exception e) {
			System.err.println("Error generating UID message.");
		}
		return sb.toString();
	}
	
	/**
	 * MESSAGEId Separator IP Separator
	 */
	public static String getLeaderPublishMessage() {
		/**
		 * MESSAGEId Separator IP Separator
		 */
		StringBuilder sb = new StringBuilder();
		try {
			sb.append(MessageMeta.LEADER).append(MessageMeta.SEPARATOR).append(
			InetAddress.getLocalHost().getHostAddress()).append(MessageMeta.SEPARATOR);
		} catch (Exception e) {
			System.err.println("Error generating LEADER message.");
		}
		return sb.toString();
	}

	/**
	 * MESSAGEId Separator IP Separator
	 */
	public static String getNonLeaderMessage() {
		/**
		 * MESSAGEId Separator IP Separator
		 */
		StringBuilder sb = new StringBuilder();
		try {
			sb.append(MessageMeta.NON_LEADER).append(MessageMeta.SEPARATOR).
			append(InetAddress.getLocalHost().getHostAddress()).append(MessageMeta.SEPARATOR);
		} catch (Exception e) {
			System.err.println("Error generating NON_LEADER message.");
		}
		return sb.toString();
	}

	/**
	 * MESSAGEId Separator IP Separator
	 */
	public static String getPulse() {
		/**
		 * MESSAGEId Separator IP Separator
		 */
		StringBuilder sb = new StringBuilder();
		try {
			sb.append(MessageMeta.PULSE).append(MessageMeta.SEPARATOR).append(
			InetAddress.getLocalHost().getHostAddress()).append(MessageMeta.SEPARATOR);
		} catch (Exception e) {
			System.err.println("Error generating PULSE message.");
		}
		return sb.toString();
	}

	/**
	 * MESSAGEId Separator IP Separator
	 */
	public static String getPing() {
		/**
		 * MESSAGEId Separator IP Separator
		 */
		StringBuilder sb = new StringBuilder();
		try {
			sb.append(MessageMeta.PING_LEADER).append(MessageMeta.SEPARATOR)
			.append(InetAddress.getLocalHost().getHostAddress()).append(MessageMeta.SEPARATOR);
		} catch (Exception e) {
			System.err.println("Error generating PING message.");
		}
		return sb.toString();
	}

	/**
	 * MESSAGEId Separator IP Separator
	 */
	public static String getRequestNotAccepted() {
		/**
		 * MESSAGEId Separator IP Separator
		 */
		StringBuilder sb = new StringBuilder();
		try {
			sb.append(MessageMeta.REQUEST_NOT_ACCEPTED).append(MessageMeta.SEPARATOR).append(
			InetAddress.getLocalHost().getHostAddress()).append(MessageMeta.SEPARATOR);
		} catch (Exception e) {
			System.err.println("Error generating REQUEST_NOT_ACCEPTED message.");
		}
		return sb.toString();
	}
	
	/** Return the message type according to the header value. When a new message
	 * type added, place the message Id here
	 * 
	 * @param messageCode
	 * @return if a valid message => message type, if not => null
	 */
	public static String getMessage(int messageCode) {
		String message = null;
		switch (messageCode) {
		case 1:
			message = "UID";
			break;
		case 2:
			message = "LEADER";
			break;
		case 3:
			message = "NON_LEADER";
			break;
		case 4:
			message = "PULSE";
			break;
		case 5:
			message = "PING";
			break;
		case 6:
			message = "REQUEST_NOT_ACCEPTED";
			break;
		default:
			break;
		}
		return message;
	}

}
