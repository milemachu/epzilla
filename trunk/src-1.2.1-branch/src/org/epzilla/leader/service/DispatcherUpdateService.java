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
package org.epzilla.leader.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Iterator;

import org.epzilla.common.discovery.dispatcher.DispatcherDiscoveryManager;
import org.epzilla.leader.Epzilla;
import org.epzilla.leader.client.DispatcherClientManager;
import org.epzilla.leader.message.RmiMessageClient;
import org.epzilla.leader.util.Status;
import org.epzilla.leader.util.SystemConstants;

/**
 * This is the update service of the dispatchers.
 * @author Harshana Eranga Martin
 *
 */
public class DispatcherUpdateService extends Thread implements IEpzillaService {
	
	// Private constructor prevents instantiation from other classes
	   private DispatcherUpdateService() {
		   this.setDaemon(true);
	   }
	   
	   public void  run() {
		while (true) {
			try {
				Thread.sleep(SystemConstants.UPDATE_SERVICE_RUNNING_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			executeService();
		}
	}
	 
	   /**
	    * SingletonHolder is loaded on the first execution of Singleton.getInstance() 
	    * or the first access to SingletonHolder.INSTANCE, not before.
	    */
	   private static class DispatcherUpdateServiceHolder { 
	     private static final DispatcherUpdateService INSTANCE = new DispatcherUpdateService();
	   }
	 
	   public static DispatcherUpdateService getInstance() {
	     return DispatcherUpdateServiceHolder.INSTANCE;
	   }
	
	@SuppressWarnings("unchecked")
	public void executeService() {
		
		System.out.println("Starting dispatcher update routine");
		
		HashSet<String> currentDispatcherList=new HashSet<String>(DispatcherClientManager.getDispatcherList());
		HashSet<String> respondedDispatcherList=new HashSet<String>();
		try {
			currentDispatcherList.remove(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		for (Iterator iterator = currentDispatcherList.iterator(); iterator.hasNext();) {
			final String string = (String) iterator.next();
			String response=RmiMessageClient.getStateFromRemote(string);
			if(response!=null){
				respondedDispatcherList.add(string);
			}			
		}
		
		if(DispatcherClientManager.getDispatcherList().size()==currentDispatcherList.size()+1){
			//No new updates to node list
			currentDispatcherList.removeAll(respondedDispatcherList);
			for (Iterator iterator = currentDispatcherList.iterator(); iterator
					.hasNext();) {
				String string = (String) iterator.next();
				DispatcherDiscoveryManager.getDispatcherPublisher().removeDispatcher(string);
				System.out.println("Removed a dead dispatcher from dispatcher list :"+string+"Removed by "+Thread.currentThread().getId());
			}
			
		}
		
		//Not really need to check cz of leader is down new one comes and registers.
		//So nvr gonna be dead unless all are down in the  cluster.
		HashSet<String> currentClusterLeaderList=new HashSet<String>(DispatcherClientManager.getClusterLeaderList().values());
		HashSet<String> respondedClusterLeaderList=new HashSet<String>();
		
		for (Iterator iterator = currentClusterLeaderList.iterator(); iterator
				.hasNext();) {
			String string = (String) iterator.next();
			String response=RmiMessageClient.getStateFromRemote(string);
			if(response!=null){
				respondedClusterLeaderList.add(string);
			}			
		}
		
		if(DispatcherClientManager.getClusterLeaderList().size()==currentClusterLeaderList.size()){
			//No new updates to node list
			currentClusterLeaderList.removeAll(respondedClusterLeaderList);
			for (Iterator iterator = currentClusterLeaderList.iterator(); iterator
					.hasNext();) {
				String string = (String) iterator.next();
				DispatcherDiscoveryManager.getDispatcherPublisher().removeLeaderSubscrition(string, "UNSUBSCRIBE_DISPATCHER_SERVICE");
				System.out.println("Removed a dead cluster leader from cluster leader list :"+string+"Removed by "+Thread.currentThread().getId());
			}
			
		}
		
		if(Epzilla.getStatus().equalsIgnoreCase(Status.LEADER.name()))
			executeLeaderTasks(respondedDispatcherList);
		
		currentDispatcherList=null;
		respondedDispatcherList=null;
		currentClusterLeaderList=null;
		respondedClusterLeaderList=null;
		System.gc();				
	}
	
	@SuppressWarnings("unchecked")
	private void executeLeaderTasks(HashSet<String> respondedList){
		System.out.println("Starting dispatcher leader update routine.");
		HashSet<String> currentSubscribedDispatcherList=new HashSet<String>(DispatcherClientManager.getSubscribedDispatcherList());

		
		if(DispatcherClientManager.getSubscribedDispatcherList().size()==currentSubscribedDispatcherList.size()){
			//No new updated for subscribed list
			currentSubscribedDispatcherList.removeAll(respondedList);
			for (Iterator iterator = currentSubscribedDispatcherList.iterator(); iterator
					.hasNext();) {
				String string = (String) iterator.next();
				DispatcherDiscoveryManager.getLeaderPublisher().removeSubscrition(string, "UNSUBSCRIBE_DISPATCHER_LEADER_SERVICE");
				System.out.println("Removed a dead dispatcher from subscribers :"+string+"Removed by "+Thread.currentThread().getId());
				
			}
		}
		
		currentSubscribedDispatcherList=null;
		respondedList=null;
	}

}
