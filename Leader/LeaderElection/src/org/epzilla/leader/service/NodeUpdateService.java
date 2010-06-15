package org.epzilla.leader.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Iterator;

import org.epzilla.common.discovery.node.NodeDiscoveryManager;
import org.epzilla.leader.Epzilla;
import org.epzilla.leader.client.NodeClientManager;
import org.epzilla.leader.message.RmiMessageClient;
import org.epzilla.leader.util.Status;
import org.epzilla.leader.util.SystemConstants;

public class NodeUpdateService extends Thread implements IEpzillaService{
	// Private constructor prevents instantiation from other classes
	 private NodeUpdateService() {
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
	   private static class NodeUpdateServiceHolder { 
	     private static final NodeUpdateService INSTANCE = new NodeUpdateService();
	   }
	 
	   public static NodeUpdateService getInstance() {
	     return NodeUpdateServiceHolder.INSTANCE;
	   }
	
	

	@SuppressWarnings("unchecked")
	public void executeService() {
		System.out.println("Starting node update routine.");
		
		HashSet<String> currentNodeList=new HashSet<String>(NodeClientManager.getNodeList());
		HashSet<String> respondedList=new HashSet<String>();
		try {
			currentNodeList.remove(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		for (Iterator iterator = currentNodeList.iterator(); iterator.hasNext();) {
			final String string = (String) iterator.next();
			String response=RmiMessageClient.getStateFromRemote(string);
			if(response!=null){
				respondedList.add(string);
			}			
		}
		
		if(NodeClientManager.getNodeList().size()==currentNodeList.size()+1){
			//No new updates to node list
			currentNodeList.removeAll(respondedList);
			for (Iterator iterator = currentNodeList.iterator(); iterator
					.hasNext();) {
				String string = (String) iterator.next();
				NodeDiscoveryManager.getNodePublisher().removeSubscrition(string, "UNSUBSCRIBE_NODE_SERVICE");
				System.out.println("Removed a dead node from node list :"+string +"Removed by "+Thread.currentThread().getId());
			}
			
		}
		
		if(Epzilla.getStatus().equalsIgnoreCase(Status.LEADER.name()))
			executeLeaderTasks(respondedList);
		
		currentNodeList=null;
		respondedList=null;
		System.gc();				
	}
	
	@SuppressWarnings("unchecked")
	private void executeLeaderTasks(HashSet<String> respondedList){
		System.out.println("Starting node leader update routine.");
		
		HashSet<String> currentSubscribedNodeList=new HashSet<String>(NodeClientManager.getSubscribedNodeList());
		HashSet<String> currentDispatcherList=new HashSet<String>(NodeClientManager.getDispatcherList());
		HashSet<String> respondedDispatcherList=new HashSet<String>();
		
		if(NodeClientManager.getSubscribedNodeList().size()==currentSubscribedNodeList.size()){
			//No new updated for subscribed list
			currentSubscribedNodeList.removeAll(respondedList);
			for (Iterator iterator = currentSubscribedNodeList.iterator(); iterator
					.hasNext();) {
				String string = (String) iterator.next();
				NodeDiscoveryManager.getLeaderPublisher().removeSubscrition(string, "UNSUBSCRIBE_LEADER_SERVICE");
				System.out.println("Removed a dead node from subscribers :"+string+"Removed by "+Thread.currentThread().getId());
				
			}
		}
		
		for (Iterator iterator = currentDispatcherList.iterator(); iterator.hasNext();) {
			final String string = (String) iterator.next();
			String response=RmiMessageClient.getStateFromRemote(string);
			if(response!=null){
				respondedDispatcherList.add(string);
			}			
		}
		
		if(NodeClientManager.getDispatcherList().size()==currentDispatcherList.size()){
			//No new dispatcher has arrived.
			currentDispatcherList.removeAll(respondedDispatcherList);
			for (Iterator iterator = currentDispatcherList.iterator(); iterator
					.hasNext();) {
				String string = (String) iterator.next();
				NodeDiscoveryManager.getLeaderPublisher().removeDispatcherList(string);
				System.out.println("Removed a dispatcher from Dispatcher list :"+string);
				
			}
		}
		
		currentSubscribedNodeList=null;
		currentDispatcherList=null;
		respondedDispatcherList=null;
		respondedList=null;
	}

}
