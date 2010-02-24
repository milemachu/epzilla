package org.epzilla.leader;

import org.epzilla.leader.message.Message;

public class TESTMAIN {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Message msg=Message.getInstance();
		System.out.println(msg.getUidMessage());
		System.out.println("");
		
		System.out.println(msg.getLeaderPublishMessage());
		System.out.println("");
		
		System.out.println(msg.getNonLeaderMessage());
		System.out.println("");

	}

}
