package org.epzilla.leader.message;



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
	public void decodeMessage(String message){
		String []strItems=message.split(Character.toString(MessageMeta.SEPARATOR));
		
		String messageName=Message.getInstance().getMessage(Integer.parseInt(strItems[0]));
		
		//Logic here to pass te message type with params to B.L.
		
		System.out.println(messageName);
	}


}
