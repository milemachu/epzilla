package org.epzilla.common.discovery.accumulator;

import org.epzilla.common.discovery.IServicePublisher;

public class AccumulatorPublisher implements IServicePublisher{

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
