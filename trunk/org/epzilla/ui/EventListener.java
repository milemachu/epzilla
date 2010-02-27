package org.epzilla.ui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Vector;

import javax.swing.JTextArea;

public class EventListener{
	ClientHandler client = new ClientHandler();
	public EventListener(){
		
	}
	public Vector<String> lookUP(String ip,String serviceName) throws MalformedURLException, RemoteException, NotBoundException{
		
		return client.getServiceIp(ip, serviceName);
	}
	public void uploadFiles(String disIP,String serviceName,String fileLocation) throws NotBoundException, IOException{
			client.uploadFile(disIP, serviceName,fileLocation);
	}
	 public void  getString(JTextArea jt) {
	        jt.append("hfdsjksfk.\n");
	        jt.append("sfsvsfs\n");
	    }
}
