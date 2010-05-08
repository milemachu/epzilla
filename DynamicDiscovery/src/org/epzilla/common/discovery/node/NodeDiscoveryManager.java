package org.epzilla.common.discovery.node;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;

import org.epzilla.common.discovery.IServicePublisher;
import org.epzilla.common.discovery.node.MulticastMessageDecoder;
import org.epzilla.common.discovery.node.TCPMessageDecoder;
import org.epzilla.common.discovery.multicast.MulticastReceiver;
import org.epzilla.common.discovery.unicast.TCPListener;

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
	
	

	public NodeDiscoveryManager(final int clusterId) {
	
		NodeDiscoveryManager.clusterId=clusterId;
		
		leaderPublisher=new LeaderPublisher();
		nodePublisher=new NodePublisher();
		
		tcpListenerThread=new Thread(new Runnable() {
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
		
		tcpListenerThread.start();
		
		multicastListnerThread=new Thread(new Runnable() {
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
		
		multicastListnerThread.start();
		
//Now Broadcast out capabilities via publisher.
		
		multicastSenderThread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					nodePublisher.publishService(clusterId);
					
					if(isLeader()){
						leaderPublisher.publishService(clusterId);
					}
					
					
				}
			}
		});
	
		multicastSenderThread.start();
	}
	
	
	
	public static IServicePublisher getPublisher(){
		if(isLeadeer)
			return leaderPublisher;
		
		return nodePublisher;
	}
	
	public static LeaderPublisher getLeaderPublisher(){
		return leaderPublisher;
	}
	
	public static NodePublisher getNodePublisher(){
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
	
	
	@SuppressWarnings({ "static-access", "unchecked" })
	public static void main(String[] args) {
		NodeDiscoveryManager nodeMan=new NodeDiscoveryManager(5);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		for (Iterator iterator = (nodeMan.getNodePublisher().getNodes().iterator()); iterator.hasNext();) {
			String str = (String) iterator.next();
			System.out.println(str);
		}
		
		nodeMan.setLeader(true);
		try {
			nodeMan.setClusterLeader(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		for (Iterator iterator = nodeMan.getLeaderPublisher().getSubscribers().iterator(); iterator.hasNext();) {
			String str = (String) iterator.next();
			System.out.println(str);
		}
	}
}
