/***************************************************************************************************
Copyright 2009 - 2010 Harshana Eranga Martin, Dishan Metihakwala, Rajeev Sampath, Chathura Randika

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
****************************************************************************************************/
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

/**
 * This is the service class where all the multicasting components will utilize this class inorder to send 
 * multicast messages to other components.  
 * @param multicastGroupIp
 * @param multicastPort
 */
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
