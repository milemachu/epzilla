package org.epzilla.leader.message;

import java.util.concurrent.ConcurrentLinkedQueue;


public class RmiMessageHandler {
	private ConcurrentLinkedQueue<String> messageQueue;
	private MessageDecoder messageDecoder;
	
	public RmiMessageHandler() {
		messageQueue=new ConcurrentLinkedQueue<String>();
		messageDecoder=new MessageDecoder();
		
		Thread executor=new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (true) {
					String message=messageQueue.poll();
					if(message!=null){
//						boolean isDecoded=false;
//						while (!isDecoded) {
//							isDecoded=
								messageDecoder.decodeMessage(message);
							//Might be able to remove as well. Aware of that.
//						}						
					}
				}
			}
		});
		
		executor.start();
	}
	
	public boolean enqueueMessage(String message){
		messageQueue.add(message);
		return true;
	}
	
	public boolean dequeueMessage(String message) {
		if(messageQueue.contains(message)){
			messageQueue.remove(message);
			return true;
		}
		return false;
	}


}
