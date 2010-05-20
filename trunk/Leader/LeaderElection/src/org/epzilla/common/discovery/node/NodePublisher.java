package org.epzilla.common.discovery.node;

import java.util.HashSet;

import org.epzilla.common.discovery.Constants;
import org.epzilla.common.discovery.IServicePublisher;
import org.epzilla.common.discovery.multicast.MulticastSender;

public class NodePublisher implements IServicePublisher {
	private String serviceName="NODE_SERVICE";	
	private String multicastGroupIp="224.0.0.2";
	private int multicastPort=5005;
	private HashSet<String> nodeList=new HashSet<String>();

	
	public boolean addSubscription(String serviceClient, String serviceName) {
		if (serviceName.equalsIgnoreCase("SUBSCRIBE_" + this.serviceName)) {
			synchronized (nodeList) {
			
				nodeList.add(serviceClient);
				return true;
			}
		}
		return false;
	}

	
	public boolean publishService() {
		MulticastSender broadcaster = new MulticastSender(multicastGroupIp,
				multicastPort);
		broadcaster.broadcastMessage(serviceName);
		return true;
	}
	
	public boolean publishService(int clusterId){
		MulticastSender broadcaster = new MulticastSender(multicastGroupIp,
				multicastPort);
		broadcaster.broadcastMessage(serviceName+Constants.CLUSTER_ID_DELIMITER+clusterId);
		return true;
	}

	
	public boolean removeSubscrition(String serviceClient, String serviceName) {
		if (serviceName.equalsIgnoreCase("UNSUBSCRIBE_" + this.serviceName)) {
			synchronized (nodeList) {
				nodeList.remove(Integer.parseInt(serviceClient));
				return true;
			}
		}

		return false;
	}
	
	public HashSet<String> getNodes(){
		return nodeList;
	}

}
