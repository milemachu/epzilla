package org.epzilla.leader.message;

import java.net.InetAddress;

import org.epzilla.leader.EpzillaProcess;

/**
 * This class defines the message format to be sent When the parameters are
 * given, it will return the message
 * 
 * @author Administrator
 * 
 */
public class Message {

	private Message() {
	}

	/**
	 * SingletonHolder is loaded on the first execution of
	 * Singleton.getInstance() or the first access to SingletonHolder.INSTANCE,
	 * not before.
	 */
	private static class MessageHolder {
		private static final Message INSTANCE = new Message();
	}

	public static Message getInstance() {
		return MessageHolder.INSTANCE;
	}

	/**
	 * MESSAGEId Separator || Separator IP Separator Status Separator
	 */

	public String getUidMessage() {
		/**
		 * MESSAGEId Separator |UID| Separator IP Separator Status Separator
		 */
		StringBuilder sb = new StringBuilder();
		try {

			sb.append(MessageMeta.UID).append(MessageMeta.SEPARATOR).append(
					EpzillaProcess.getInstance().getUID()).append(MessageMeta.SEPARATOR);
			sb.append(InetAddress.getLocalHost().getHostAddress()).append(
					MessageMeta.SEPARATOR).append(
					EpzillaProcess.getInstance().getStatus());
			sb.append(MessageMeta.SEPARATOR);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	public String getLeaderPublishMessage() {
		/**
		 * MESSAGEId Separator IP Separator
		 */
		StringBuilder sb = new StringBuilder();
		try {

			sb.append(MessageMeta.LEADER).append(MessageMeta.SEPARATOR).append(
					InetAddress.getLocalHost().getHostAddress()).append(
					MessageMeta.SEPARATOR);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	public String getNonLeaderMessage() {
		/**
		 * MESSAGEId Separator IP Separator
		 */
		StringBuilder sb = new StringBuilder();
		try {

			sb.append(MessageMeta.NON_LEADER).append(MessageMeta.SEPARATOR)
					.append(InetAddress.getLocalHost().getHostAddress())
					.append(MessageMeta.SEPARATOR);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	public String getPulse() {

		/**
		 * MESSAGEId Separator IP Separator
		 */
		StringBuilder sb = new StringBuilder();
		try {

			sb.append(MessageMeta.PULSE).append(MessageMeta.SEPARATOR).append(
					InetAddress.getLocalHost().getHostAddress()).append(
					MessageMeta.SEPARATOR);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();

	}

	public String getPing() {
		/**
		 * MESSAGEId Separator IP Separator
		 */
		StringBuilder sb = new StringBuilder();
		try {

			sb.append(MessageMeta.PING_LEADER).append(MessageMeta.SEPARATOR)
					.append(InetAddress.getLocalHost().getHostAddress())
					.append(MessageMeta.SEPARATOR);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();

	}

	public String getRequestNotAccepted() {
		/**
		 * MESSAGEId Separator IP Separator
		 */
		StringBuilder sb = new StringBuilder();
		try {

			sb.append(MessageMeta.REQUEST_NOT_ACCEPTED).append(
					MessageMeta.SEPARATOR).append(
					InetAddress.getLocalHost().getHostAddress()).append(
					MessageMeta.SEPARATOR);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();

	}

	/**
	 * Return the message type according to the header value. When a new message
	 * type added, place the message Id here
	 * 
	 * @param messageCode
	 * @return if a valid message => message type, if not => null
	 */
	public String getMessage(int messageCode) {
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
