package org.epzilla.common.discovery.dispatcher;

import java.util.Hashtable;

import org.epzilla.common.discovery.Constants;
import org.epzilla.common.discovery.IServicePublisher;

public class DispatcherPublisher implements IServicePublisher {
	private String serviceName="DISPATCHER_SERVICE";	
	private Hashtable<Integer, String> clusterLeaderIp=new Hashtable<Integer, String>();

	public void addSubscription(String serviceClient, String serviceName) {
		if(serviceName.equalsIgnoreCase(this.serviceName)){
			synchronized (clusterLeaderIp) {
				String  []arr=serviceClient.split(Constants.DISPATCHER_CLIENT_DELIMITER);
				clusterLeaderIp.put(Integer.parseInt(arr[0]), arr[1]);
			}
		}
			

	}

	public void publishService() {
	}

	public void removeSubscrition(String serviceClient, String serviceName) {
		if(serviceName.equalsIgnoreCase(this.serviceName)){
			synchronized (clusterLeaderIp){
				clusterLeaderIp.remove(Integer.parseInt(serviceClient.split(Constants.DISPATCHER_CLIENT_DELIMITER)[0]));
			}
		}
	}

}
