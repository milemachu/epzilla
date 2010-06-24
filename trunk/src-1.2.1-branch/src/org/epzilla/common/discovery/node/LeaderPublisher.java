package org.epzilla.common.discovery.node;

import java.util.HashSet;

import org.epzilla.common.discovery.Constants;
import org.epzilla.common.discovery.IServicePublisher;
import org.epzilla.common.discovery.multicast.MulticastSender;

/**
 * This is the leader service publisher for the cluster node and this class is used to collect details of the other nodes in the same 
 * cluster and the dispatchers in the system.
 * @author Administrator
 *
 */
public class LeaderPublisher implements IServicePublisher {
	private static String LEADER_SERVICE_NAME = "LEADER_SERVICE";
	private String multicastGroupIp = "224.0.0.2";
    private static String SUBSCRIBE_PREFIX = "SUBSCRIBE_";
	private static String UNSUBSCRIBE_PREFIX = "UNSUBSCRIBE_";
	private int multicastPort = 5005;
	private HashSet<String> clusterNodeIp = new HashSet<String>();
	private HashSet<String> dispatcherIp = new HashSet<String>();

	public boolean addSubscription(String serviceClient, String serviceName) {
		if (serviceName.equalsIgnoreCase(SUBSCRIBE_PREFIX + LEADER_SERVICE_NAME)) {
			synchronized (clusterNodeIp) {				
				clusterNodeIp.add(serviceClient);
				System.out.println("New Cluster Node Discovered by Cluster Leader : "+serviceClient);
				return true;
			}
		}
		return false;
	}

	public boolean publishService() {
		MulticastSender broadcaster = new MulticastSender(multicastGroupIp,multicastPort);
		broadcaster.broadcastMessage(LEADER_SERVICE_NAME);
		return true;
	}
	
	public boolean publishService(int clusterId){
		MulticastSender broadcaster = new MulticastSender(multicastGroupIp,multicastPort);
		broadcaster.broadcastMessage(LEADER_SERVICE_NAME+Constants.CLUSTER_ID_DELIMITER+clusterId);
		return true;
	}

	public boolean removeSubscrition(String serviceClient, String serviceName) {
		if (serviceName.equalsIgnoreCase(UNSUBSCRIBE_PREFIX + LEADER_SERVICE_NAME)) {
			synchronized (clusterNodeIp) {
				clusterNodeIp.remove(serviceClient);
				return true;
			}
		}
		return false;
	}

	public boolean updateDispatcherList(String dispatcherIpToInsert) {
		synchronized (dispatcherIp) {
			dispatcherIp.add(dispatcherIpToInsert);
			System.out.println("New Dispatcher Discovered: "+dispatcherIpToInsert);
			return true;
		}
	}

	public boolean removeDispatcherList(String dispatcherIpToRemove) {
		synchronized (dispatcherIp) {
			dispatcherIp.remove(dispatcherIpToRemove);
			return true;
		}
	}

	public HashSet<String> getSubscribers() {
		return clusterNodeIp;
	}

	public HashSet<String> getDispatchers() {
		return dispatcherIp;
	}
}
