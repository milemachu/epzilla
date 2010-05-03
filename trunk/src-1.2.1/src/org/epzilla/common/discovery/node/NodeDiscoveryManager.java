package org.epzilla.common.discovery.node;

import org.epzilla.common.discovery.IServicePublisher;
import org.epzilla.common.discovery.multicast.MulticastReceiver;

public class NodeDiscoveryManager {
	
	Thread tcpListenerThread;
	Thread multicastListnerThread;
	Thread multicastSenderThread;
	int tcpPort=5010;
	String multicastGroupIp="224.0.0.2";
	int multicastPort=5005;
	static LeaderPublisher leaderPublisher;
	static NodePublisher nodePublisher;
	static boolean isLeadeer=false;
	static int clusterId;
	static String clusterLeader;
	
	

	public NodeDiscoveryManager(int clusterId) {
	
		NodeDiscoveryManager.clusterId=clusterId;
		
		leaderPublisher=new LeaderPublisher();
		nodePublisher=new NodePublisher();
		
		multicastListnerThread=new Thread(new Runnable() {
			@SuppressWarnings("unused")
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
	
	public static void setLeader(boolean result){
		NodeDiscoveryManager.isLeadeer=result;
	}
	
	public static int getClusterId(){
		return clusterId;
	}
	
	public static String getClusterLeader() {
		return clusterLeader;
	}
	
	public static void setClusterLeader(String clusterLeader){
		NodeDiscoveryManager.clusterLeader=clusterLeader;
	}
	
}
