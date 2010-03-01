package org.epzilla.leader;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.epzilla.leader.message.Message;
import org.epzilla.leader.message.MessageDecoder;

public class TESTMAIN {

	/**
	 * @param args
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException {
		Message msg=Message.getInstance();
		System.out.println(msg.getUidMessage());
		System.out.println("");
		
		System.out.println(msg.getLeaderPublishMessage());
		System.out.println("");
		
		System.out.println(msg.getNonLeaderMessage());
		System.out.println("");
		
		EpzillaProcess.getInstance().getClusterIpList()	.add(InetAddress.getLocalHost());
		
		System.out.println(EpzillaProcess.getInstance().getClusterIpList().get(0));
		
		MessageDecoder.getInstance().decodeMessage(msg.getUidMessage());

	}

}
