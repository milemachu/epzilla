package org.epzilla.common.discovery.dispatcher;

import java.util.HashSet;

import org.epzilla.common.discovery.IServicePublisher;
import org.epzilla.common.discovery.multicast.MulticastSender;

public class DispatcherLeaderPublisher implements IServicePublisher {
	private static String DISPATCHER_LEADER_SERVICE_NAME = "DISPATCHER_LEADER_SERVICE";
    private static String SUBSCRIBE_PREFIX="SUBSCRIBE_";
    private static String UNSUBSCRIBE_PREFIX="UNSUBSCRIBE_";
	private String multicastGroupIp = "224.0.0.2";
	private int multicastPort = 5005;
	private HashSet<String> dispatcherIpList = new HashSet<String>();

	@Override
	public boolean addSubscription(String serviceClient, String serviceName) {
		if (serviceName.equalsIgnoreCase(SUBSCRIBE_PREFIX+DISPATCHER_LEADER_SERVICE_NAME)) {
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
		broadcaster.broadcastMessage(DISPATCHER_LEADER_SERVICE_NAME);
		return true;
	}

	@Override
	public boolean removeSubscrition(String serviceClient, String serviceName) {
		if (serviceName.equalsIgnoreCase(UNSUBSCRIBE_PREFIX+DISPATCHER_LEADER_SERVICE_NAME)) {
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
