package org.epzilla.leader.message;

public class MTMessageDecoder implements Runnable {

	private String message;

	public MTMessageDecoder(String message) {
		this.message = message;
	}

	private void DecodeMessage() {

		String[] strItems = message.split(Character
				.toString(MessageMeta.SEPARATOR));

		String messageName = Message.getInstance().getMessage(
				Integer.parseInt(strItems[0]));

		// Logic here to pass te message type with params to B.L.

		System.out.println(messageName);
	}

	public void run() {
		DecodeMessage();

	}

}
