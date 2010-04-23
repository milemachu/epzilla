package org.epzilla.common.discovery;

/**
 * This interface is used to publish services via ip multicasting.
 * Provide ability to add and remove subscribers to services for the service providers.
 * Applications have to implement this interface according to the requirements.
 * @author Administrator
 *
 */
public interface IServicePublisher {
	
	
	public void publishService();
	
	public void addSubscription(String serviceClient, String serviceName);
	
	public void removeSubscrition(String serviceClient, String serviceName);

}
