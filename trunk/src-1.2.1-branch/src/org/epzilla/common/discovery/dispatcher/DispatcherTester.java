package org.epzilla.common.discovery.dispatcher;

import org.epzilla.common.discovery.multicast.MulticastSender;

public class DispatcherTester {
	private static String multicastGroupIp="224.0.0.2";
	private static int multicastPort=5005;
	private static String serviceName="DISPATCHER_SERVICE";	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MulticastSender broadcaster=new MulticastSender(multicastGroupIp,multicastPort);
		for (int i = 0; i < 5; i++) {
			broadcaster.broadcastMessage(serviceName);
		}
	}

}
