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
package org.epzilla.leader.message;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This is the RMI message handler where all the received RMI messages are queued here and our observer 
 * will enqueue the messages and start decoding the messages.
 * @author Harshana Eranga Martin
 *
 */
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
					}else{
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
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
