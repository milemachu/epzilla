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

import java.util.Iterator;

import org.epzilla.common.discovery.Constants;
import org.epzilla.common.discovery.multicast.MulticastReceiver;
import org.epzilla.common.discovery.unicast.TCPListener;
import org.epzilla.common.discovery.unicast.TCPSender;
import org.epzilla.leader.util.SystemConstants;

/**
 * This class is the manager class of the dynamic discovery component for the dispatcher.
 * Initiating Capability multicasting and initiating TCP listener for subscription for cluster leaders.
 * This is the interface which provide access to the dynamic discovery data of the dispatchers. 
 * @author Harshana Eranga Martin 	 mailto: harshana05@gmail.com
 *
 */
public class DispatcherDiscoveryManager {
	
	Thread tcpThread;
	Thread mcListenerThread;
	Thread multicastThread;
	int tcpPort=5010;
	private String multicastGroupIp="224.0.0.2";
	private int multicastPort=5005;
	static DispatcherPublisher dispatcherPublisher;
	static DispatcherLeaderPublisher leaderPublisher;
	static String dispatcherLeader;
	static boolean isLeader=false;
	static boolean isSubscribedWithLeader=false;
	
	public DispatcherDiscoveryManager() {
		dispatcherPublisher=new DispatcherPublisher();
		leaderPublisher=new DispatcherLeaderPublisher();
		
		//Starting the TCP Listener
		tcpThread=new Thread(new Runnable() {
			TCPListener tcpListner;
			@Override
			public void run() {
				tcpListner=new TCPListener(tcpPort); 
				
				while (true) {
					String messageReceived=tcpListner.MessageReceived();
					Thread executor=new Thread(new TCPMessageDecoder(messageReceived));
					executor.start();
				}
			}
		});
		
		tcpThread.start();
		
		//Starting the multicast listener
		mcListenerThread=new Thread(new Runnable() {
			MulticastReceiver mcReceiver;
			@Override
			public void run() {
				mcReceiver=new MulticastReceiver(multicastGroupIp, multicastPort);
				
				while (true) {
					String messageReceived=mcReceiver.messageReceived();
					Thread executor=new Thread(new MulticastMessageDecoder(messageReceived));
					executor.start();
				}
			}
		});
		
		mcListenerThread.start();
		
		//Now Broadcast out capabilities via publisher.
		
		multicastThread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(SystemConstants.DISCOVERY_MULTICAST_TIME);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					dispatcherPublisher.publishService();
					
					if(isLeader()){
						leaderPublisher.publishService();
					}
				}
			}
		});
	
		multicastThread.start();
	}
	
	public static DispatcherPublisher getDispatcherPublisher(){
		return dispatcherPublisher;
	}
	
	public static DispatcherLeaderPublisher getLeaderPublisher(){
		return leaderPublisher;
	}
	
	public static boolean isLeader(){
		return isLeader;
	}
	
	public static void setLeader(boolean result){
		DispatcherDiscoveryManager.isLeader=result;
	}
	
	public static boolean isSubscribedWithLeader() {
		return isSubscribedWithLeader;
	}

	public static void setSubscribedWithLeader(boolean isSubscribedWithLeader) {
		DispatcherDiscoveryManager.isSubscribedWithLeader = isSubscribedWithLeader;
	}

	public static String getDispatcherLeader() {
		return dispatcherLeader;
	}

	public static void setDispatcherLeader(String dispatcherLeader) {
		DispatcherDiscoveryManager.dispatcherLeader = dispatcherLeader;
	}

    //test method
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		DispatcherDiscoveryManager dm=new DispatcherDiscoveryManager();
		
		TCPSender ts=new TCPSender("127.0.0.1", 5010);
		ts.sendMessage("5"+Constants.DISPATCHER_CLIENT_DELIMITER+"SUBSCRIBE_DISPATCHER_SERVICE");
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(DispatcherDiscoveryManager.getDispatcherPublisher().getSubscribers().get(5));
		
		TCPSender ts1=new TCPSender("127.0.0.1", 5010);
		ts1.sendMessage("5"+Constants.DISPATCHER_CLIENT_DELIMITER+"UNSUBSCRIBE_DISPATCHER_SERVICE");
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(DispatcherDiscoveryManager.getDispatcherPublisher().getSubscribers().get(5));
		
		TCPSender ts2=new TCPSender("127.0.0.1", 5010);
		ts2.sendMessage("5"+Constants.DISPATCHER_CLIENT_DELIMITER+"SUBSCRIBE_DISPATCHER_SERVICE");
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(DispatcherDiscoveryManager.getDispatcherPublisher().getSubscribers().get(5));
		
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(DispatcherDiscoveryManager.getDispatcherPublisher().getDispatchers().isEmpty());
		System.out.println(DispatcherDiscoveryManager.getDispatcherPublisher().getDispatchers().size());

		for (Iterator iterator = DispatcherDiscoveryManager.getDispatcherPublisher().getDispatchers().iterator(); iterator.hasNext();) {
			String s = (String) iterator.next();
			System.out.println(s);
			
		}
		
	}

}
