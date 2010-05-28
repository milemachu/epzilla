package org.epzilla.common.discovery.dispatcher;

import java.util.Iterator;

import org.epzilla.common.discovery.Constants;
import org.epzilla.common.discovery.multicast.MulticastReceiver;
import org.epzilla.common.discovery.unicast.TCPListener;
import org.epzilla.common.discovery.unicast.TCPSender;
import org.epzilla.leader.util.SystemConstants;

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
	
	public DispatcherDiscoveryManager() {
		dispatcherPublisher=new DispatcherPublisher();
		leaderPublisher=new DispatcherLeaderPublisher();
		
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
	
	public static String getDispatcherLeader() {
		return dispatcherLeader;
	}

	public static void setDispatcherLeader(String dispatcherLeader) {
		DispatcherDiscoveryManager.dispatcherLeader = dispatcherLeader;
	}

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
