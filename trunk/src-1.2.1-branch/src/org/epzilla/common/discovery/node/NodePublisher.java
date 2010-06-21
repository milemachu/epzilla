package org.epzilla.common.discovery.node;

import java.util.HashSet;

import org.epzilla.common.discovery.Constants;
import org.epzilla.common.discovery.IServicePublisher;
import org.epzilla.common.discovery.multicast.MulticastSender;

public class NodePublisher implements IServicePublisher {
	private static String NODE_SERVICE_NAME="NODE_SERVICE";
    private static String SUBSCRIBE_PREFIX = "SUBSCRIBE_";
	private static String UNSUBSCRIBE_PREFIX = "UNSUBSCRIBE_";
	private String multicastGroupIp="224.0.0.2";
	private int multicastPort=5005;
	private HashSet<String> nodeList=new HashSet<String>();

	
	public boolean addSubscription(String serviceClient, String serviceName) {
		if (serviceName.equalsIgnoreCase(SUBSCRIBE_PREFIX + NODE_SERVICE_NAME)) {
			synchronized (nodeList) {			
				nodeList.add(serviceClient);
				System.out.println("New Cluster Node Discovered: "+serviceClient);
				return true;
			}
		}
		return false;
	}

	
	public boolean publishService() {
		MulticastSender broadcaster = new MulticastSender(multicastGroupIp,
				multicastPort);
		broadcaster.broadcastMessage(NODE_SERVICE_NAME);
		return true;
	}
	
	public boolean publishService(int clusterId){
		MulticastSender broadcaster = new MulticastSender(multicastGroupIp,
				multicastPort);
		broadcaster.broadcastMessage(NODE_SERVICE_NAME+Constants.CLUSTER_ID_DELIMITER+clusterId);
		return true;
	}

	
	public boolean removeSubscrition(String serviceClient, String serviceName) {
		if (serviceName.equalsIgnoreCase(UNSUBSCRIBE_PREFIX + NODE_SERVICE_NAME)) {
			synchronized (nodeList) {
				nodeList.remove(serviceClient);
				return true;
			}
		}

		return false;
	}
	
	public HashSet<String> getNodes(){
		return nodeList;
	}

}
