package org.epzilla.leader;

import java.util.HashSet;
import java.util.Vector;

import org.epzilla.leader.client.NodeClientManager;
import org.epzilla.leader.event.listner.EpZillaListener;
import org.epzilla.leader.event.listner.IEpzillaEventListner;
import org.epzilla.leader.util.Status;

public class EpzillaLeaderPubSub {
	
	private static HashSet<String> clientList;
	
	private	static Vector<IEpzillaEventListner> clientListenerList =new Vector<IEpzillaEventListner>();
	
	public static void initializePubSub(){
		//TODO: Need to handle this according to the component. Eg: IF node one, if Dis some thig else.
		clientList=new HashSet<String>(NodeClientManager.getSubscribedNodeList());
		
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
			clientListenerList.removeAllElements();
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

}
