package org.epzilla.leader.client;

import java.util.HashSet;
import java.util.Hashtable;

import org.epzilla.common.discovery.dispatcher.DispatcherDiscoveryManager;


public class DispatcherClientManager {

	private DispatcherDiscoveryManager dispatcherDiscMgr;
	
	public DispatcherClientManager() {
		setDispatcherDiscMgr(new DispatcherDiscoveryManager());
	}
	
	public static HashSet<String> getDispatcherList(){
		return DispatcherDiscoveryManager.getDispatcherPublisher().getDispatchers();
	}
	
	public static Hashtable<Integer, String> getClusterLeaderList() {
		return DispatcherDiscoveryManager.getDispatcherPublisher().getSubscribers();
	}

	public void setDispatcherDiscMgr(DispatcherDiscoveryManager dispatcherDiscMgr) {
		this.dispatcherDiscMgr = dispatcherDiscMgr;
	}

	public DispatcherDiscoveryManager getDispatcherDiscMgr() {
		return dispatcherDiscMgr;
	}
	
	
	
}
