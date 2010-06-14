package org.epzilla.common.discovery.multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MulticastSender {
private DatagramSocket datagramSocket;
private DatagramPacket datagramPacket;
private String multicastGroupIp;
private int multicastPort;


public MulticastSender(String multicastGroupIp, int multicastPort) {
	
	this.multicastGroupIp=multicastGroupIp;
	this.multicastPort=multicastPort;
	
	
}

public void broadcastMessage(String message){
	try {
		datagramSocket=new DatagramSocket();	
		datagramPacket=new DatagramPacket(message.getBytes(), message.getBytes().length, InetAddress.getByName(multicastGroupIp), multicastPort);
		datagramSocket.send(datagramPacket);
	} catch (UnknownHostException e) {
		System.err.println(e.getMessage());
	} catch (IOException e) {
		System.err.println(e.getMessage());
	} catch (Exception e) {
		System.err.println(e.getMessage());
	}	
	datagramSocket.close();
	datagramSocket=null;
	datagramPacket=null;	
}

public static void main(String[] args) {
	MulticastSender ms=new MulticastSender("224.0.0.2", 5005);
	ms.broadcastMessage("TestMessage");
}
}
