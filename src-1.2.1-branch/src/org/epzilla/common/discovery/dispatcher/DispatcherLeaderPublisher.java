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
package org.epzilla.common.discovery.dispatcher;

import java.util.HashSet;

import org.epzilla.common.discovery.IServicePublisher;
import org.epzilla.common.discovery.multicast.MulticastSender;

/**
 * This is the publisher class of the dispatcher leader and this is a concete implementation of IServicePulisher interface.
 * This class uses to maintain the details about the subscribed dispatchers with the dispatcher leader.  
 * @author Harshana Eranga Martin 	 mailto: harshana05@gmail.com
 *
 */
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
