package org.epzilla.test.multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastReciever implements Runnable{
	
	private MulticastSocket mc;
	private DatagramPacket dp;
	private byte[] recievedArr;
	private int port;
	private InetAddress sender;
	private boolean isRecieved;
	private String packetSender;
	
	public MulticastReciever(InetAddress sender, int port) {
		this.sender=sender;
		this.port=port;
		
		try {
			mc=new MulticastSocket(this.port);
			mc.joinGroup(sender);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		
		System.err.println("Waiting for Multicast messages to recieve....");
		
		isRecieved =false;
		
		
		while(true){
			try {
				isRecieved=true;
				
				recievedArr=new byte[512];
				dp=new DatagramPacket(recievedArr, recievedArr.length);
				mc.receive(dp);
				
				System.out.println(new String(dp.getData()));
				
				packetSender=dp.getAddress().getHostAddress();
				
				recievedArr=null;
				dp=null;
				
				System.gc();			
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
		}
		
	}

	public boolean isRecieved() {
		return isRecieved;
	}

	public void setRecieved(boolean isRecieved) {
		this.isRecieved = isRecieved;
	}

	public String getPacketSender() {
		return packetSender;
	}

	public void setPacketSender(String packetSender) {
		this.packetSender = packetSender;
	}

	public InetAddress getSender() {
		return sender;
	}

	public void setSender(InetAddress sender) {
		this.sender = sender;
	}
	

	
	
}
