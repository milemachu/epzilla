package org.epzilla.leader.message;



/**
 * This class will decode the incoming message from the others to RMI server of local host.
 * It will send the result to the Business Logic of the algorithm and do the work.
 * @author Administrator
 *
 */
public class MessageDecorder {
	private String message;
	
	public MessageDecorder(String message) {
		this.message=message;
	}
	
	public void decodeMessage(){
		String []strItems=message.split(Character.toString(MessageMeta.SEPARATOR));
		
		String messageName=Message.getInstance().getMessage(Integer.parseInt(strItems[0]));
		
		//Logic here to pass te message type with params to B.L.
		
		System.out.println(messageName);
	}

}
