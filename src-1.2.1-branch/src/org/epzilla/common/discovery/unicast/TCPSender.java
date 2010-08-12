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
package org.epzilla.common.discovery.unicast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.epzilla.common.discovery.Constants;

/**
 * This is tge TCP message sending service class and all the tcp sending components use this class as their service provider. 
 * @author Harshana Eranga Martin 	 mailto: harshana05@gmail.com
 *
 */
public class TCPSender {
	
	private Socket socket;
	private String tcpRemoteIp;
	private int tcpPort;
	
	public TCPSender(String tcpRemoteIp,int tcpPort) {
		this.setTcpRemoteIp(tcpRemoteIp);
		this.setTcpPort(tcpPort);
	}
	
	public void sendMessage(String message){
		try {
			socket=new Socket(tcpRemoteIp, tcpPort);
			socket.getOutputStream().write(message.getBytes());
			socket.getOutputStream().flush();			
			socket.close();
			socket=null;
			
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public static void main(String[] args) {
		try {
			TCPSender ts=new TCPSender(InetAddress.getLocalHost().getHostAddress(), 5010);
			ts.sendMessage("5"+Constants.DISPATCHER_CLIENT_DELIMITER+"DISPATCHER_SERVICE");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}

	public void setTcpRemoteIp(String tcpRemoteIp) {
		this.tcpRemoteIp = tcpRemoteIp;
	}

	public String getTcpRemoteIp() {
		return tcpRemoteIp;
	}

	public void setTcpPort(int tcpPort) {
		this.tcpPort = tcpPort;
	}

	public int getTcpPort() {
		return tcpPort;
	}

}
