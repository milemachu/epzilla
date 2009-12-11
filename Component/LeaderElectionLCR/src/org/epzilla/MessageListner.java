package org.epzilla;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.epzilla.util.Constant;

public class MessageListner implements Runnable{

	private int serverPort= 5055;
	private ServerSocket serverSoc;
	private Socket clientSoc;
	private byte[] array;
	
	public MessageListner() {
		try {
			serverSoc=new ServerSocket(serverPort);	
			array=new byte[256];
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void processServerMessage() {

		// Processing message
		System.err.println("Listening..");
		
		String result=null;

		try {
			
				clientSoc = serverSoc.accept();
				clientSoc.getInputStream().read(array);
				String recivedMessage=new String(array);
				
				ArrayList<String> itemList=MessageDecoder.getMessageItems(recivedMessage);
				
				// LCR Logic here and send the reply from this as well
				int senderPriority=Integer.parseInt(itemList.get(1));
				
				if(InetAddress.getLocalHost().getHostAddress().equals(itemList.get(0))){
					
					result="Priority is high and the same host";
				}else{
					if(Constant.priority==senderPriority){
						result="Priority is equals. Network configuration is incorrect.";
					}else if(Constant.priority>senderPriority){
						result="Priority is low.Terminate process.";
					}else if(Constant.priority<senderPriority){
						result="Priority is high. Continue process.";
					}
				}
				
				
				clientSoc.getOutputStream().write(result.getBytes());
				clientSoc.getOutputStream().flush();
				//This is it
				
				System.err.println("Message Recived and decoded");
				System.err.println(itemList.get(0)+ " "+itemList.get(1));
				
				clientSoc.close();
				clientSoc=null;
				
				System.gc();
				
				
				
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		processServerMessage();
		
	}

}
