package org.epzilla.leader.client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

import org.epzilla.common.discovery.dispatcher.DispatcherDiscoveryManager;
import org.epzilla.leader.Epzilla;


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
	
	public static String getNextDispatcher(){
		HashSet<String> discoveredSet=new HashSet<String>(getDispatcherList());
		ArrayList<String> staticIpList=new ArrayList<String>(Epzilla.getComponentIpList().values());
//		Hashtable<Integer,String> staticHT=new Hashtable<Integer, String>(Epzilla.getComponentIpList());
		String myIp=null;
		try {
			myIp=InetAddress.getLocalHost().getHostAddress();
			discoveredSet.add(myIp);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		staticIpList.retainAll(discoveredSet);
//		ArrayList<String> liveIpList=new ArrayList<String>(staticHT.values());
		int myIndex=staticIpList.indexOf(myIp);
	
		if(myIndex==staticIpList.size()-1){
		//Last index
			return staticIpList.get(0);
		}else{
			return staticIpList.get(myIndex+1);
		}
	}
	
}
