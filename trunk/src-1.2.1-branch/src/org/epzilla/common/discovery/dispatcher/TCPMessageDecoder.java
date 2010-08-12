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

import org.epzilla.common.discovery.Constants;
import org.epzilla.leader.Epzilla;

/**
 * This is the TCP message decoder where the messages received to the dispatcher tcp listener are sent here to decode.
 * Then required actions are taken according to the messages.
 * @author Harshana Eranga Martin
 *
 */
public class TCPMessageDecoder implements Runnable {

	private String message;
    private static String DISPATCHER_SERVICE_NAME="DISPATCHER_SERVICE";
    private static String DISPATCHER_LEADER_SERVICE_NAME="DISPATCHER_LEADER_SERVICE";
    private static String SUBSCRIBE_PREFIX="SUBSCRIBE_";
    private static String UNSUBSCRIBE_PREFIX="UNSUBSCRIBE_";
    private HashSet<String> authorizedList=new HashSet<String>(Epzilla.getComponentIpList().values());
	
	public TCPMessageDecoder(String message) {
		this.message=message;
	}
	
	@Override
	public void run() {
		//0=message,1=ip
		String []tcpArr=message.split(Constants.TCP_UNICAST_DELIMITER);
		if(tcpArr[0].equalsIgnoreCase(SUBSCRIBE_PREFIX+DISPATCHER_LEADER_SERVICE_NAME) && DispatcherDiscoveryManager.isLeader() && authorizedList.contains(tcpArr[1])){
			DispatcherDiscoveryManager.getLeaderPublisher().addSubscription(tcpArr[1], tcpArr[0]);
		}else if(tcpArr[0].equalsIgnoreCase(UNSUBSCRIBE_PREFIX+DISPATCHER_LEADER_SERVICE_NAME) && DispatcherDiscoveryManager.isLeader() && authorizedList.contains(tcpArr[1])){
			DispatcherDiscoveryManager.getLeaderPublisher().removeSubscrition(tcpArr[1], tcpArr[0]);
		}else{
			//0=cluster id,1=service name
			String []arr=tcpArr[0].split(Constants.DISPATCHER_CLIENT_DELIMITER);
			if(arr[1].equalsIgnoreCase(SUBSCRIBE_PREFIX+DISPATCHER_SERVICE_NAME)){
				DispatcherDiscoveryManager.getDispatcherPublisher().addSubscription(arr[0]+Constants.DISPATCHER_CLIENT_DELIMITER+tcpArr[1], arr[1]);
			}else if(arr[1].equalsIgnoreCase(UNSUBSCRIBE_PREFIX+DISPATCHER_LEADER_SERVICE_NAME)){
				DispatcherDiscoveryManager.getDispatcherPublisher().removeSubscrition(arr[0]+Constants.DISPATCHER_CLIENT_DELIMITER+tcpArr[1], arr[1]);
			}
		}
	}

}
