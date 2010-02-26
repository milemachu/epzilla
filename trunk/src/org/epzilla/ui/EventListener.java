package org.epzilla.ui;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.Vector;

public class EventListener {
	ClientHandler client = new ClientHandler();
	public EventListener(){
		
	}
	public Vector<String> lookUP(String ip,String serviceName){
		return client.getService(ip, serviceName);
	}
	public void uploadFiles(String disIP,String serviceName,String fileLoction){
		try {
			client.uploadFile(disIP, serviceName,fileLoction);
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
