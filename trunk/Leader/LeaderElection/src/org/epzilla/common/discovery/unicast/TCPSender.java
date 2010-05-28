package org.epzilla.common.discovery.unicast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.epzilla.common.discovery.Constants;

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
