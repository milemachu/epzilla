package org.epzilla.leader;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.epzilla.leader.util.Status;

public class Epzilla {
	private static long UID=0;
	private static int clusterId=0;
	private static String clusterLeader=null;
	private static String status=Status.UNKNOWN.name();
	private static String defaultLeader=null;
	private static boolean isLeaderElectionRunning=false;
	
	public static long getUID() {
		return UID;
	}
	
	public static void setUID(long UID) {
		Epzilla.UID=UID;
	}
	
	public static int getClusterId(){
		return clusterId;
	}
	
	public static void setClusterID(int clusterId){
		Epzilla.clusterId=clusterId;
	}
	
	public static String getClusterLeader() {
		synchronized (clusterLeader) {
			return clusterLeader;
		}
	}

	public static void setClusterLeader(String clusterLeader) {
		synchronized (Epzilla.clusterLeader) {
			Epzilla.clusterLeader=clusterLeader;
		}
	}
	
	public static String getStatus() {
		synchronized (status) {
			return status;
		}
	}
	
	public static void setStatus(String status) {
		synchronized (Epzilla.status) {
			Epzilla.status=status;
		}
	}
	
	public static String getDefaultLeader() {
		synchronized (defaultLeader) {
			return defaultLeader;
		}
	}
	
	public static void setDefaultLeader(String defaultLeader) {
		synchronized (Epzilla.defaultLeader) {
			Epzilla.defaultLeader=defaultLeader;
		}
	}
	
	public static boolean isDefaultLeader(){
		try {
			if(Epzilla.defaultLeader.equalsIgnoreCase(InetAddress.getLocalHost().getHostAddress()))
				return true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}		
		return false;
	}
	
	public static boolean isLeader() {
		synchronized (clusterLeader) {
			synchronized (status) {
				try {
					if(clusterLeader.equalsIgnoreCase(InetAddress.getLocalHost().getHostAddress()) && status.equalsIgnoreCase(Status.LEADER.name())){
						return true;
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			}				
		}		
		return false;		
	}
	
	public static boolean isLeaderElectionRunning() {
		synchronized (status) {
			if(status.equalsIgnoreCase(Status.UNKNOWN.name()) && isLeaderElectionRunning)
				return true;
		}		
		return false;
	}
	
	public static void setLeaderElectionRunning(boolean isLeaderElectionRunning) {
		synchronized (status) {
			if(status.equalsIgnoreCase(Status.UNKNOWN.name()) && !isLeaderElectionRunning)
				Epzilla.isLeaderElectionRunning=true;			
		}
		
	}
}
