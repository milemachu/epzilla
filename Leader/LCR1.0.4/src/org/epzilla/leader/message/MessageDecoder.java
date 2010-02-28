package org.epzilla.leader.message;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.epzilla.leader.EpzillaProcess;
import org.epzilla.leader.Status;



/**
 * This class will decode the incoming message from the others to RMI server of local host.
 * It will send the result to the Business Logic of the algorithm and do the work.
 * @author Administrator
 *
 */
public class MessageDecoder{
	
	// Private constructor prevents instantiation from other classes
	   private MessageDecoder() {}
	 
	   /**
	    * SingletonHolder is loaded on the first execution of Singleton.getInstance() 
	    * or the first access to SingletonHolder.INSTANCE, not before.
	    */
	   private static class MessageDecorderHolder { 
	     private static final MessageDecoder INSTANCE = new MessageDecoder();
	   }
	 
	   public static MessageDecoder getInstance() {
	     return MessageDecorderHolder.INSTANCE;
	   }
	
	   // This method will decode the message 
	public void decodeMessage(String message) throws UnknownHostException{
		String []strItems=message.split(Character.toString(MessageMeta.SEPARATOR));
		
		String messageType=Message.getInstance().getMessage(Integer.parseInt(strItems[0]));
		System.out.println("New Message Received: "+messageType);
		
		//Logic here to pass the message type with parameters to B.L.
		
		//Elected leader send message
		if(Integer.parseInt(strItems[0])==MessageMeta.LEADER){
			//This message is from Leader and to Leader publish msg and received at electedLeader.
			EpzillaProcess.getInstance().setClusterLeader(InetAddress.getByName(strItems[1]));
			if(EpzillaProcess.getInstance().getClusterLeader().getHostAddress().equalsIgnoreCase(InetAddress.getLocalHost().getHostAddress())){
				EpzillaProcess.getInstance().setStatus(Status.LEADER.toString());
				System.out.println("Leader is the Localhost");
			}
			else{
				EpzillaProcess.getInstance().setStatus(Status.NON_LEADER.toString());
				System.out.println("Leader is else and this is  a NON leader.");
			
			}
			
			System.out.println("Leader published and updated at the local instance.");
			System.out.println("Leader is "+ EpzillaProcess.getInstance().getClusterLeader());
			System.out.println("Local host is "+InetAddress.getLocalHost());
			System.out.println("Local host Status:"+EpzillaProcess.getInstance().getStatus());
			
			//Now create a Thread to count down for the pulse
			//When PULSE received reset the count down
		}else if(Integer.parseInt(strItems[0])==MessageMeta.UID){
			//Initiate the Leader Election and Set the status to unknown
			// Init the cluster leader
			EpzillaProcess.getInstance().setClusterLeader(null);
			EpzillaProcess.getInstance().setStatus(Status.UNKNOWN.toString());
			
		}
		

	}


}
