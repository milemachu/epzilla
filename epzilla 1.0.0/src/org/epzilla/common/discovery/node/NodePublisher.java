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
package org.epzilla.common.discovery.node;

import java.util.HashSet;

import org.epzilla.common.discovery.Constants;
import org.epzilla.common.discovery.IServicePublisher;
import org.epzilla.common.discovery.multicast.MulticastSender;

/**
 * This is the node service publisher class and all the nodes implement this class and multicast the node service using this class.
 * @author Harshana Eranga Martin 	 mailto: harshana05@gmail.com
 *
 */
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
