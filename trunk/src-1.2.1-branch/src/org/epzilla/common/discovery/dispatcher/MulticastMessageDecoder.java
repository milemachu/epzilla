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
import org.epzilla.common.discovery.unicast.TCPSender;
import org.epzilla.leader.Epzilla;

/**
 * This is the multicast message decoder for the dispatcher and all the messages received to the dispatcher multicast listener.
 * @author Harshana Eranga Martin
 *
 */
public class MulticastMessageDecoder implements Runnable {
//Handle the incoming broadcast messages from other dispatchers to elect the leader.
	private String message;
	private int tcpPort=5010;
    private static String DISPATCHER_SERVICE_NAME="DISPATCHER_SERVICE";
    private static String DISPATCHER_LEADER_SERVICE_NAME="DISPATCHER_LEADER_SERVICE";
    private static String SUBSCRIBE_PREFIX="SUBSCRIBE_";
    private HashSet<String> authorizedList=new HashSet<String>(Epzilla.getComponentIpList().values());
	
	public MulticastMessageDecoder(String message) {
		this.message=message;
	}
	@Override
	public void run() {
		//0=message;1=sender
		String []mcArr=message.split(Constants.MULTICAST_DELIMITER);
		if(mcArr[0].equalsIgnoreCase(DISPATCHER_SERVICE_NAME) && !DispatcherDiscoveryManager.getDispatcherPublisher().getDispatchers().contains(mcArr[1]) && authorizedList.contains(mcArr[1])){
			DispatcherDiscoveryManager.getDispatcherPublisher().insertDispatcher(mcArr[1]);
		}else if(mcArr[0].equalsIgnoreCase(DISPATCHER_LEADER_SERVICE_NAME) && !DispatcherDiscoveryManager.isSubscribedWithLeader() && !DispatcherDiscoveryManager.isLeader() && authorizedList.contains(mcArr[1])){
			DispatcherDiscoveryManager.setDispatcherLeader(mcArr[1]);
			DispatcherDiscoveryManager.setSubscribedWithLeader(true);
			//send a tcp msg to subscribe with it.
			TCPSender ts=new TCPSender(mcArr[1], tcpPort);
			ts.sendMessage(SUBSCRIBE_PREFIX+DISPATCHER_LEADER_SERVICE_NAME);
		}
	}
}
