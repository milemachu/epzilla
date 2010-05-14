package org.epzilla.leader;

import java.util.HashSet;

import org.epzilla.leader.client.DispatcherClientManager;
import org.epzilla.leader.client.NodeClientManager;
import org.epzilla.leader.event.listner.EpZillaListener;
import org.epzilla.leader.event.listner.IEpzillaEventListner;
import org.epzilla.leader.util.Component;
import org.epzilla.leader.util.Status;

public class EpzillaLeaderPubSub {
	
	private static HashSet<String> clientList;
	
	private	static HashSet<IEpzillaEventListner> clientListenerList =new HashSet<IEpzillaEventListner>();
	
	public static void initializePubSub(){
		if(Epzilla.getComponentType().equalsIgnoreCase(Component.NODE.name())){
			clientList=new HashSet<String>(NodeClientManager.getSubscribedNodeList());
		}else if(Epzilla.getComponentType().equalsIgnoreCase(Component.DISPATCHER.name())){
			clientList=new HashSet<String>(DispatcherClientManager.getDispatcherList());
		}
		
		synchronized (clientList) {
			synchronized (clientListenerList) {
				for (String node : clientList) {
					clientListenerList.add(new EpZillaListener(node));
				}
			}
		}
	}
	
	public static void resetPubSub(){
		synchronized (clientList) {
			clientList=null;
		}
		synchronized (clientListenerList) {
			clientListenerList.clear();
		}
	}
	
	public static void addClientListner(IEpzillaEventListner listener){
		synchronized (clientListenerList) {
			if(Epzilla.getStatus().equalsIgnoreCase(Status.LEADER.name()))
			clientListenerList.add(listener);
		}
	}
	
	public static boolean removeClientListner(IEpzillaEventListner listener){
		synchronized (clientListenerList) {
			if(clientListenerList.contains(listener)){
				clientListenerList.remove(listener);
				return true;
			}			
		}		
		return false;		
	}
	
	public static HashSet<IEpzillaEventListner> getClientListenerList() {
		synchronized (clientListenerList) {
			return clientListenerList;
		}		
	}

}
