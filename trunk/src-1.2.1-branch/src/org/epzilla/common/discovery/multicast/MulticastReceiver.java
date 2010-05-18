package org.epzilla.common.discovery.multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import org.epzilla.common.discovery.Constants;
import org.epzilla.util.Logger;

public class MulticastReceiver {
	
	private String multicastGroupIp;
	private int multicastPort;
	private MulticastSocket mcSocket;
	private DatagramPacket datagramPacket;
	private String multicastSenderIp;
	private byte []receivedData;
	
	public MulticastReceiver(String multicastGroupIp,int multicastPort) {
		this.setMulticastGroupIp(multicastGroupIp);
		this.setMulticastPort(multicastPort);
		
		
		try{
			mcSocket=new MulticastSocket(multicastPort);
			mcSocket.joinGroup(InetAddress.getByName(multicastGroupIp));
		}catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	/**
	 * This method is used to receive the Multicast messages from other nodes.
	 * @return received message with format, RECEIVED_DATA+MULTICAST_DELIMITER+MULTICAST_MESSAGE_SENDER.
	 */
	public String messageReceived(){
		
		receivedData=new byte[512];
		datagramPacket=new DatagramPacket(receivedData, receivedData.length);
		try {
			mcSocket.receive(datagramPacket);
			
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		multicastSenderIp=datagramPacket.getAddress().getHostAddress();
		StringBuilder sb=new StringBuilder();
		sb.append(new String(datagramPacket.getData()).trim()).append(Constants.MULTICAST_DELIMITER).append(multicastSenderIp);
		
		datagramPacket=null;
		receivedData=null;
		
		return sb.toString();
	}
	
	public static void main(String[] args) {
		MulticastReceiver rec=new MulticastReceiver("224.0.0.2", 5005);
		Logger.log(rec.messageReceived());
	}

	public void setMulticastGroupIp(String multicastGroupIp) {
		this.multicastGroupIp = multicastGroupIp;
	}

	public String getMulticastGroupIp() {
		return multicastGroupIp;
	}

	public void setMulticastPort(int multicastPort) {
		this.multicastPort = multicastPort;
	}

	public int getMulticastPort() {
		return multicastPort;
	}

}
