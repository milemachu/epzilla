package org.epzilla.common.discovery.dispatcher;

import org.epzilla.common.discovery.Constants;
import org.epzilla.common.discovery.unicast.TCPListener;
import org.epzilla.common.discovery.unicast.TCPSender;

public class DispatcherDiscoveryManager {
	
	Thread tcpThread;
	Thread multicastThread;
	int tcpPort=5010;
	static DispatcherPublisher publisher;
	
	public DispatcherDiscoveryManager() {
		publisher=new DispatcherPublisher();
		
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
		
		//Now Broadcast out capabilities via publisher.
		
		multicastThread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					publisher.publishService();
				}
			}
		});
	
		multicastThread.start();
	}
	
	public static DispatcherPublisher getDispatcherPublisher(){
		return publisher;
	}
	
	public static void main(String[] args) {
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
		
	}

}
