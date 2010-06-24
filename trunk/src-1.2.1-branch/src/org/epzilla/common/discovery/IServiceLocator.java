package org.epzilla.common.discovery;

/**
 * This interface is used to locate remote services using IP multicasting 
 * This provides subscribe and unsubscribe to services for service clients.
 * Application has to implement this interface according to the requirements.
 * @author Administrator
 *
 */
public interface IServiceLocator {
	
	public String searchService(String serviceName);
	
	public String requestSubscription(String serviceName);
	
	public String requestUnsunscription(String serviceName);

}
