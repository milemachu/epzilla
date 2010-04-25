package org.epzilla.common.discovery.node;

import org.epzilla.common.discovery.IServicePublisher;

public class NodePublisher implements IServicePublisher {
	private String serviceName="NODE_SERVICE";	
	private String multicastGroupIp="224.0.0.2";
	private int multicastPort=5005;

	@Override
	public boolean addSubscription(String serviceClient, String serviceName) {
		return false;
	}

	@Override
	public boolean publishService() {
		return false;
	}

	@Override
	public boolean removeSubscrition(String serviceClient, String serviceName) {
		return false;
	}

}
