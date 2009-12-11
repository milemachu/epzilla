package org.epzilla;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.epzilla.util.Constant; 

public class MessageSender implements Runnable{
	
private Socket clientSoc;
private String ipAddress;
private int serverPort = 5055;
//private String messageDelimeter="#";
//private int priority=1;
private byte[] array;

public MessageSender(String ipAddress){
	this.ipAddress=ipAddress;
	array=new byte[256];
	
	
}
	public String sendMessage(){
		try {
			clientSoc=new Socket(ipAddress, serverPort);
			clientSoc.getOutputStream().write(getLeaderMessage().getBytes());
			clientSoc.getOutputStream().flush();
			
			System.err.println("Sent....");
			
			//Read the reply as well
			clientSoc.getInputStream().read(array);
			System.out.println("Response is "+ new String(array));
			
			//This is it
			
			clientSoc.close();
			clientSoc=null;
			System.gc();
			
			return new String(array);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	private String getLeaderMessage(){
		
		StringBuilder sb=new StringBuilder();
		try {
			sb.append(InetAddress.getLocalHost().getHostAddress()).append(Constant.LeaedrMessageParamDelimeter).append(Constant.priority).append(Constant.LeaderMessageDelimeter);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	
	public void run() {
		sendMessage();
		
	}
	
	
}
