package org.epzilla.leader;

import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.util.HashSet;

import org.epzilla.leader.client.DispatcherClientManager;
import org.epzilla.leader.client.NodeClientManager;
import org.epzilla.leader.rmi.LeaderInterface;
import org.epzilla.leader.rmi.LeaderServiceImpl;
import org.epzilla.leader.util.Component;
import org.epzilla.leader.util.ConfigurationLoader;

public class LeaderElectionInitiator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LeaderElectionInitiator LE=new LeaderElectionInitiator();
		
		boolean isServiceDeployed=LE.deployLeaderRmiService();
		if(isServiceDeployed){
			ConfigurationLoader config=new ConfigurationLoader();
			config.loadConfig();
			
			//Starting Dynamic Discovery
			final String comType=Epzilla.getComponentType();
			if(comType.equalsIgnoreCase(Component.NODE.name())){
				@SuppressWarnings("unused")
				NodeClientManager nodeClientMgr=new NodeClientManager();
				NodeClientManager.setClusterLeader(Epzilla.getClusterLeader());
			}else if(comType.equalsIgnoreCase(Component.DISPATCHER.name())){
				@SuppressWarnings("unused")
				DispatcherClientManager dispClientMgr=new DispatcherClientManager();
			}
			
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			HashSet<String> set1; // Node list
			HashSet<String> set2; //Subs ist
			HashSet<String> set3; //Disp list
		
			if(comType.equalsIgnoreCase(Component.NODE.name())){
				set1=NodeClientManager.getNodeList();
				set2=NodeClientManager.getSubscribedNodeList();
				set3=NodeClientManager.getDispatcherList();
			}else{
				set1=DispatcherClientManager.getDispatcherList();
				set2=(HashSet<String>) DispatcherClientManager.getClusterLeaderList().values();
				set3=DispatcherClientManager.getDispatcherList();
			}
			
			if(set1!=null && set2!=null && set3 !=null)
				System.out.println(comType+ " Leader host");
			else if(set1!=null && set2==null && set3 !=null)
				System.out.println(comType+" non leader node");
			
			if(set1!=null && set2!=null && set3 !=null && set1.size()==set3.size())
				System.out.println("Disp");
			
			
			
		}else{
			System.out.print("Leader RMI service cannot deploy. Exiting System.");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.print(".");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(".");
			System.exit(-1);
		}
	}
	
	private boolean deployLeaderRmiService(){
		try {
			if (System.getSecurityManager() == null) {
				System.setSecurityManager(new RMISecurityManager());
			}

			LeaderInterface impl = new LeaderServiceImpl();
			Naming.rebind("rmi://"+ InetAddress.getLocalHost().getHostAddress()+ "/LeaderService", impl);
			System.out.println("Leader election RMI service successfully deployed and running.");

			return true;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

}
