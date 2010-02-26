package org.epzilla.client;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.StringTokenizer;
import java.util.Vector;

import org.epzilla.nameserver.*;

public class ClientHandler {
	
	InetAddress dispatcher;
	int dispatcherPort;
	Vector<String> dispArray=new Vector<String>();
	
	 public Vector<String> getService(){
	    	
	       	try {
	       		NameService r = (NameService)Naming.lookup("NameServer");
	    		int size = r.getDirectorySize();
				for(int i=0; i<size;i++){
				String ip = r.getHostName(i);
				int port = r.getPort(i);
				dispArray.add(ip +" "+port);
				
				}
	       	}
			catch (IOException e) {
				e.printStackTrace();
			} catch (NotBoundException e) {
				e.printStackTrace();
			}
			return dispArray;
	    }
	public static void main(String[] args) {
	ClientHandler myClient = new ClientHandler();
	myClient.getService();
	}

}
