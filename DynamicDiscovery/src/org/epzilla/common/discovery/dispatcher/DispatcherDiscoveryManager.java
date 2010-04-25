package org.epzilla.common.discovery.dispatcher;

import org.epzilla.common.discovery.Constants;
import org.epzilla.common.discovery.unicast.TCPListener;
import org.epzilla.common.discovery.unicast.TCPSender;

public class DispatcherDiscoveryManager {
	
	Thread tcpThread;
	Thread multicastThread;
	static DispatcherPublisher publisher;
	
	public DispatcherDiscoveryManager() {
		publisher=new DispatcherPublisher();
		
		tcpThread=new Thread(new Runnable() {
			TCPListener tcpListner;
			@Override
			public void run() {
				tcpListner=new TCPListener(5010); 
				
				while (true) {
					String messageReceived=tcpListner.MessageReceived();
					Thread executor=new Thread(new TCPMessageDecoder(messageReceived));
					executor.start();
				}
			}
		});
		
		tcpThread.start();
		
		
	
	}
	
	public static DispatcherPublisher getDispatcherPublisher(){
		return publisher;
	}
	
	public static void main(String[] args) {
		DispatcherDiscoveryManager dm=new DispatcherDiscoveryManager();
		TCPSender ts=new TCPSender("127.0.0.1", 5010);
		ts.sendMessage("5"+Constants.DISPATCHER_CLIENT_DELIMITER+"DISPATCHER_SERVICE");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(DispatcherDiscoveryManager.getDispatcherPublisher().getSubscribers().get(5));
	}

}
