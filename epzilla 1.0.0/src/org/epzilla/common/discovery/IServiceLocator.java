/***************************************************************************************************
Copyright 2009 - 2010 Harshana Eranga Martin, Dishan Metihakwala, Rajeev Sampath, Chathura Randika

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
****************************************************************************************************/
package org.epzilla.common.discovery;

/**
 * This interface is used to locate remote services using IP multicasting 
 * This provides subscribe and unsubscribe to services for service clients.
 * Application has to implement this interface according to the requirements.
 * @author Harshana Eranga Martin 	 mailto: harshana05@gmail.com
 *
 */
public interface IServiceLocator {
	
	public String searchService(String serviceName);
	
	public String requestSubscription(String serviceName);
	
	public String requestUnsunscription(String serviceName);

}
