package org.epzilla.common.discovery.node;

import org.epzilla.common.discovery.IServicePublisher;
import org.epzilla.common.discovery.multicast.MulticastReceiver;

public class NodeDiscoveryManager {
	
	Thread tcpListenerThread;
	Thread multicastListnerThread;
	Thread multicastSenderThread;
	int tcpPort=5020;
	String multicastGroupIp="224.0.0.3";
	int multicastPort=5015;
	static LeaderPublisher leaderPublisher;
	static NodePublisher nodePublisher;
	static boolean isLeadeer=false;
	static int clusterId;
	
	

	public NodeDiscoveryManager(int clusterId) {
	
		NodeDiscoveryManager.clusterId=clusterId;
		
		leaderPublisher=new LeaderPublisher();
		nodePublisher=new NodePublisher();
		
		multicastListnerThread=new Thread(new Runnable() {
			MulticastReceiver mcReceiver;
			@Override
			public void run() {
				mcReceiver=new MulticastReceiver(multicastGroupIp, multicastPort);
				
				while (true) {
					
				}
			}
		});
	}
	
	public static IServicePublisher getPublisher(){
		if(isLeadeer)
			return leaderPublisher;
		
		return nodePublisher;
	}
	
	public static boolean isLeader(){
		return isLeadeer;
	}
	
	public static int getClusterId(){
		return clusterId;
	}
	
}
