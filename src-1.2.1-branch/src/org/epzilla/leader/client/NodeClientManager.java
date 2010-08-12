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
package org.epzilla.leader.client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;

import org.epzilla.common.discovery.node.NodeDiscoveryManager;
import org.epzilla.leader.Epzilla;

/**
 * This class is using the API provided by the node discovery manager class in the dynamic service discovery component and wrap the 
 * functionalities and provides a higher layer API for the dynamic discovery services for nodes.
 * 
 * @author Harshana Eranga Martin
 *
 */
public class NodeClientManager {

	private NodeDiscoveryManager nodeDiscMgr;
	
	public NodeClientManager() {
		setNodeDiscMgr(new NodeDiscoveryManager(Epzilla.getClusterId()));
	}
	
	public static HashSet<String> getNodeList(){
		return NodeDiscoveryManager.getNodePublisher().getNodes();
	}
	
	public static String getClusterLeader() {
		return NodeDiscoveryManager.getClusterLeader();
	}
	
	public static void setClusterLeader(String clusterLeader){
		try {
			if(InetAddress.getLocalHost().getHostAddress().equalsIgnoreCase(clusterLeader))
				NodeDiscoveryManager.setLeader(true);
			else
				NodeDiscoveryManager.setLeader(false);
			NodeDiscoveryManager.setClusterLeader(clusterLeader);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}
	
	public static HashSet<String> getDispatcherList() {
		return NodeDiscoveryManager.getLeaderPublisher().getDispatchers();
	}
	
	public static HashSet<String> getSubscribedNodeList() {
		return	NodeDiscoveryManager.getLeaderPublisher().getSubscribers();
	}

	public void setNodeDiscMgr(NodeDiscoveryManager nodeDiscMgr) {
		this.nodeDiscMgr = nodeDiscMgr;
	}

	public NodeDiscoveryManager getNodeDiscMgr() {
		return nodeDiscMgr;
	}
	
	public static void setSubscribedWithLeader(boolean result){
		NodeDiscoveryManager.setSubscribedWithLeader(result);
	}
	
	public static boolean isSubscribedWithLeader(){
		return NodeDiscoveryManager.isSubscribedWithLeader();
	}
	
	public static String getNextNode(){
		HashSet<String> discoveredSet=new HashSet<String>(getNodeList());
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
