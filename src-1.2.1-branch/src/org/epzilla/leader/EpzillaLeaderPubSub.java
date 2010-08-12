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
package org.epzilla.leader;

import java.util.HashSet;

import org.epzilla.leader.client.DispatcherClientManager;
import org.epzilla.leader.client.NodeClientManager;
import org.epzilla.leader.event.listner.EpZillaListener;
import org.epzilla.leader.event.listner.IEpzillaEventListner;
import org.epzilla.leader.util.Component;
import org.epzilla.leader.util.Status;

/**
 * This is the class which keeps tack of the nodes which are subscribed with the leader via RMI messaging. 
 * Whenever the Leader is publisher and the report message is sent to the non leader nodes, they are registering their listener with
 * the leader and the registered listeners are stored in here.
 * @author Harshana Eranga Martin
 *
 */
public class EpzillaLeaderPubSub {
	
	private static HashSet<String> clientList=new HashSet<String>();
	
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
			clientList.clear();
		}
		synchronized (clientListenerList) {
			clientListenerList.clear();
		}
	}
	
	public static boolean addClientListner(IEpzillaEventListner listener){
		synchronized (clientListenerList) {
			if(Epzilla.getStatus().equalsIgnoreCase(Status.LEADER.name())){
				clientListenerList.add(listener);
				return true;
			}
			return false;
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
