package org.epzilla.leader.client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;

import org.epzilla.common.discovery.node.NodeDiscoveryManager;
import org.epzilla.leader.Epzilla;

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
	
	public static String getNextNode(){
		return null;
		//TODO: Complete this.
	}
	
}
