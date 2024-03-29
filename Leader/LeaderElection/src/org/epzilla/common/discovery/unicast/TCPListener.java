package org.epzilla.common.discovery.unicast;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.epzilla.common.discovery.Constants;

/**
 * This is the TCP message listening service class and all the tcp message receivers in the system uses this class as the service provider.
 * @author Administrator
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
