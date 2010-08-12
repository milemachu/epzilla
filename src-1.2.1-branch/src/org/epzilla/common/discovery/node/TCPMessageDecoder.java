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
import org.epzilla.leader.Epzilla;

/**
 * This is the TCP message decoder of the node component.
 * @author Harshana Eranga Martin
 *
 */
public class TCPMessageDecoder implements Runnable {

	private String message;
    private static String LEADER_SERVICE_NAME= "LEADER_SERVICE";
    private static String SUBSCRIBE_PREFIX = "SUBSCRIBE_";
	private static String UNSUBSCRIBE_PREFIX = "UNSUBSCRIBE_";
	private HashSet<String> authorizedNodeList=new HashSet<String>(Epzilla.getComponentIpList().values());

	public TCPMessageDecoder(String message) {
		this.message=message;
	}
	
	public void run() {
		//0=message,1=ip
		String []tcpArr=message.split(Constants.TCP_UNICAST_DELIMITER);
		if(tcpArr[0].equalsIgnoreCase(SUBSCRIBE_PREFIX+LEADER_SERVICE_NAME) && NodeDiscoveryManager.isLeader() &&  authorizedNodeList.contains(tcpArr[1])){
			
				NodeDiscoveryManager.getLeaderPublisher().addSubscription(tcpArr[1], tcpArr[0]);
			
		}else if(tcpArr[0].equalsIgnoreCase(UNSUBSCRIBE_PREFIX+LEADER_SERVICE_NAME) && NodeDiscoveryManager.isLeader() &&  authorizedNodeList.contains(tcpArr[1])){
			NodeDiscoveryManager.getLeaderPublisher().removeSubscrition(tcpArr[1], tcpArr[0]);
		}
	}

}
