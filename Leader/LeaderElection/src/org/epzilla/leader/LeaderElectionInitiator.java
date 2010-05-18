package org.epzilla.leader;

import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.util.HashSet;

import org.epzilla.leader.client.DispatcherClientManager;
import org.epzilla.leader.client.NodeClientManager;
import org.epzilla.leader.event.ProcessStatusChangedEvent;
import org.epzilla.leader.event.PulseReceivedEvent;
import org.epzilla.leader.event.listner.EpZillaListener;
import org.epzilla.leader.message.EventHandler;
import org.epzilla.leader.message.RmiMessageClient;
import org.epzilla.leader.rmi.LeaderInterface;
import org.epzilla.leader.rmi.LeaderServiceImpl;
import org.epzilla.leader.util.Component;
import org.epzilla.leader.util.ConfigurationLoader;
import org.epzilla.leader.util.Status;

public class LeaderElectionInitiator {
	
	private static EventHandler eventHandler;
	
	public LeaderElectionInitiator() {
		eventHandler=new EventHandler();
	}

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
//				NodeClientManager.setClusterLeader(Epzilla.getClusterLeader());
			}else if(comType.equalsIgnoreCase(Component.DISPATCHER.name())){
				@SuppressWarnings("unused")
				DispatcherClientManager dispClientMgr=new DispatcherClientManager();
			}
			
			//Waiting till the dynamic Discovery discovers the other nodes,Leader and dispatchers.
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//Check whether a leader is there.
			if(comType.equalsIgnoreCase(Component.NODE.name())){
				final String clusterLeader=NodeClientManager.getClusterLeader();
				if(clusterLeader!=null){
					//No LE required. Leader Exist. Join the STM. Set the Epzilla variables.
					Epzilla.setClusterLeader(clusterLeader);
					Epzilla.setStatus(Status.NON_LEADER.name());
					Epzilla.setLeaderElectionRunning(false);
					eventHandler.fireEpzillaEvent(new ProcessStatusChangedEvent());
					
					Thread registrar=new Thread(new Runnable() {
						public void run() {
							RmiMessageClient.registerListenerWithLeader(clusterLeader, new EpZillaListener());
						}
					});
					registrar.start();
					eventHandler.fireEpzillaEvent(new PulseReceivedEvent(clusterLeader));
				}else{
					System.out.println("No Leader Exist.");
					boolean isDefaultLeaderNode=Epzilla.isDefaultLeader();
					
					if(isDefaultLeaderNode){
						//There is no other leader present and This is the default leader.
						Epzilla.setLeaderElectionRunning(true);
						Epzilla.setStatus(Status.UNKNOWN.name());
						eventHandler.fireEpzillaEvent(new ProcessStatusChangedEvent());						
						final String nextNode=NodeClientManager.getNextNode();
						
						Thread starter=new Thread(new Runnable() {
							public void run() {
								RmiMessageClient.sendUidMessage(nextNode);
							}
						});
						starter.start();
						
					}else{//NOT_DEFAULT_NODE
						//Node type, No leader present, not the default leader
						final String defaultLeaderNode=Epzilla.getDefaultLeader();
						String defaultLeaderNodeStatus=null;
						@SuppressWarnings("unused")
						boolean defaultLeaderRunningLE=true;
						
						//No choice but to send message without threading. :(
						defaultLeaderNodeStatus=RmiMessageClient.getStateFromRemote(defaultLeaderNode);
						//If this is the leader, it should be already discovered by the DD.
						if(defaultLeaderNodeStatus.equalsIgnoreCase(Status.LEADER.name())){
							//TODO:Continue Here. Refer above.
						}
						
						
					}
				}
			}else{//NOT_NODE
			}
			
			@SuppressWarnings("unused")
			HashSet<String> set1; // Node list
			@SuppressWarnings("unused")
			HashSet<String> set2; //Subs ist
			@SuppressWarnings("unused")
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
