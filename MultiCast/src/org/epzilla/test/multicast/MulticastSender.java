package org.epzilla.test.multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class MulticastSender implements Runnable{
	
	private DatagramSocket ds;
	private DatagramPacket dp;
	private InetAddress reciever;
	private int recievingPort;
	private String textToSend;
	
	
	public MulticastSender(InetAddress reciever,int port) {
		this.reciever=reciever;
		this.recievingPort=port;
		try {
			ds=new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	
	public void run() {
		
		System.err.println("Multicast sending started.....");
		
		boolean isFirstTime=true;
	
	while(true){		
		
		if(isFirstTime)
			textToSend="Hello World";
		else
			textToSend="Server Alive";
		
		isFirstTime=false;
		
		dp=new DatagramPacket(textToSend.getBytes(), textToSend.getBytes().length, reciever, recievingPort);
		try {
			ds.send(dp);
			Thread.sleep(5000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
		
		
	}
	
	

}
