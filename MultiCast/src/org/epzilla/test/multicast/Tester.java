package org.epzilla.test.multicast;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Tester {
	
	private static InetAddress multicastAddr;
	private static int multicastPort= 5005;

	/**
	 * @param args
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException {
		multicastAddr=InetAddress.getByName("224.0.0.2");
		
		MulticastReciever mr=new MulticastReciever(multicastAddr, multicastPort);
		Thread reciever=new Thread(mr);
		reciever.start();
		
		MulticastSender ms=new MulticastSender(multicastAddr, multicastPort);
		Thread sender=new Thread(ms);
		sender.start();

	}

}
