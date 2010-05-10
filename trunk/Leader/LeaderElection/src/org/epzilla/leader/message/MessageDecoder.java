package org.epzilla.leader.message;

public class MessageDecoder {
	@SuppressWarnings("unused")
	private EventHandler eventHandler;
	
	public MessageDecoder() {
		eventHandler=new EventHandler();
	}

	public boolean decodeMessage(String message){
		
		//0=MessageCode
		String[] strItems = message.split(Character.toString(MessageMeta.SEPARATOR));
		String messageType=MessageGenerator.getMessage(Integer.parseInt(strItems[0]));
		System.out.println("Decoding the received message "+messageType);
		
		//Starting the decoding process
		
		
		return false;
	}
}
