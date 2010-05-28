package org.epzilla.common.discovery.dispatcher;

import java.util.HashSet;

import org.epzilla.common.discovery.IServicePublisher;
import org.epzilla.common.discovery.multicast.MulticastSender;

public class DispatcherLeaderPublisher implements IServicePublisher {
	private String serviceName = "DISPATCHER_LEADER_SERVICE";
	private String multicastGroupIp = "224.0.0.2";
	private int multicastPort = 5005;
	private HashSet<String> dispatcherIpList = new HashSet<String>();

	@Override
	public boolean addSubscription(String serviceClient, String serviceName) {
		if (serviceName.equalsIgnoreCase("SUBSCRIBE_" + this.serviceName)) {
			synchronized (dispatcherIpList) {				
				dispatcherIpList.add(serviceClient);
				System.out.println("New Dispatcher Discovered by Dispatcher Leader : "+serviceClient);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean publishService() {
		MulticastSender broadcaster = new MulticastSender(multicastGroupIp,multicastPort);
		broadcaster.broadcastMessage(serviceName);
		return true;
	}

	@Override
	public boolean removeSubscrition(String serviceClient, String serviceName) {
		if (serviceName.equalsIgnoreCase("UNSUBSCRIBE_" + this.serviceName)) {
			synchronized (dispatcherIpList) {
				dispatcherIpList.remove(serviceClient);
				return true;
			}
		}
		return false;
	}
	
	public HashSet<String> getSubscribers() {
		return dispatcherIpList;
	}

}
