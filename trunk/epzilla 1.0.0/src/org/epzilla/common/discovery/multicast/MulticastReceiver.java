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
import java.net.InetAddress;
import java.net.MulticastSocket;

import org.epzilla.common.discovery.Constants;

/**
 * This is a service class where all the multicasting components will use this class to receive the incoming
 * multicasting messages from other components. 
 * @author Harshana Eranga Martin 	 mailto: harshana05@gmail.com
 *
 */
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
		System.out.println(rec.messageReceived());
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
