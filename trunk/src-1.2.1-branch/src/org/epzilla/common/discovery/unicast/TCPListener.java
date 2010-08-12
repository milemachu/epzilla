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
import java.net.ServerSocket;
import java.net.Socket;

import org.epzilla.common.discovery.Constants;

/**
 * This is the TCP message listening service class and all the tcp message receivers in the system uses this class as the service provider.
 * @author Harshana Eranga Martin
 *
 */
public class TCPListener {
	
	private ServerSocket serverSocket;
	private Socket socket;
	private int tcpPort;
	private byte []receivedData;
	
	public TCPListener(int tcpPort) {
		this.setTcpPort(tcpPort);
		try {
			serverSocket=new ServerSocket(tcpPort);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String MessageReceived(){
		
		receivedData=new byte[512];
		try {
			socket=serverSocket.accept();
			socket.getInputStream().read(receivedData);
			
			StringBuilder sb=new StringBuilder();
			String remoteSocket=socket.getRemoteSocketAddress().toString();
			sb.append(new String(receivedData).trim()).append(Constants.TCP_UNICAST_DELIMITER).append(remoteSocket.substring(remoteSocket.indexOf("/")+1, remoteSocket.indexOf(":")));
			
			receivedData =null;
			socket.close();
			socket=null;		
			
			return sb.toString();
			
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return null;
	}
	
	public static void main(String[] args) {
		TCPListener tl=new TCPListener(5010);
		System.out.println(tl.MessageReceived());
	}

	public void setTcpPort(int tcpPort) {
		this.tcpPort = tcpPort;
	}

	public int getTcpPort() {
		return tcpPort;
	}

}
