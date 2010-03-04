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
	public void uploadEventsFiles(String disIP,String serviceName,String fileLocation,String clientIp,int eventID) throws NotBoundException, IOException{
			client.uploadEventsFile(disIP, serviceName,fileLocation,clientIp,eventID);
	}
	public void uploadTriggersFiles(String disIP,String serviceName,String fileLocation,String clientIp,int triggerID) throws NotBoundException, IOException{
		client.uploadTriggersFile(disIP, serviceName,fileLocation,clientIp,triggerID);
}
}
