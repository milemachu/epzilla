package org.epzilla.leader;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.epzilla.leader.event.IEpzillaEvent;
import org.epzilla.leader.util.Status;

/**
 * This is the epzilla core class and this class contains all the variables and methods require to maintain the state of the epzilla process.
 * This class provide the API to the epzilla process. 
 * Whenever the Leader election happens, the LE algorithm updates the variables in this class.
 * RMI  interfaces also provide API for some of the details stored in this class.
 * @author Administrator
 *
 */
public class Epzilla {
	private static long UID=0; //At init
	private static int clusterId=0; //At init
	private static String clusterLeader=""; //While running
	private static String status=Status.UNKNOWN.name(); //While running
	private static String defaultLeader=""; //At init
	private static boolean isLeaderElectionRunning=false; //While running
	private static String componentType=""; //At init
	private static Hashtable<Integer, String> componentIpList=new Hashtable<Integer, String>(); //At init
	private static ConcurrentLinkedQueue<IEpzillaEvent> timerQueue=new ConcurrentLinkedQueue<IEpzillaEvent>(); //While running
	
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

	public static void setComponentType(String componentType) {
		Epzilla.componentType = componentType;
	}

	public static String getComponentType() {
		return componentType;
	}
	
	public static boolean addToTimerQueue(IEpzillaEvent timerTask){
		return timerQueue.add(timerTask);		
	}
	
	public static boolean removeFromTimerQueue(IEpzillaEvent timerTask) {
		return timerQueue.remove(timerTask);
	}
	
	public static void resetTimerQueue(){
		timerQueue.clear();
	}

	public static void setComponentIpList(Hashtable<Integer, String> componentIpList) {
		synchronized (componentIpList) {
			Epzilla.componentIpList = componentIpList;
		}
		
	}

	public static Hashtable<Integer, String> getComponentIpList() {
		synchronized (componentIpList) {
			return componentIpList;
		}		
	}

	
}
