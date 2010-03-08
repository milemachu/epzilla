package org.epzilla.ui;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Vector;
import org.epzilla.nameserver.*;
import org.epzilla.dispatcher.*;

public class ClientHandler {
	
	InetAddress dispatcher;
	int dispatcherPort;
	Vector<String> dispIP=new Vector<String>();
	String ip ="";
	String dispServiceName="";
	String dispDetails = "";
	ClientCallbackInterface obj;
	public Vector<String> getServiceIp(String serverIp,String serviceName) throws MalformedURLException, RemoteException, NotBoundException{
	       		String url = "rmi://"+serverIp+"/"+serviceName;
	       		NameService r = (NameService)Naming.lookup(url);
	    		int size = r.getDirectorySize();
				for(int i=0; i<size;i++){
				ip = r.getHostName(i);
				dispServiceName = r.getNames(i);
				dispDetails=ip+" "+dispServiceName;
				dispIP.add(dispDetails);
				System.out.println(dispIP);
				}
				return dispIP;
	    }
	public void uploadEventsFile(String ip,String serviceName,String fileLocation,String clientIp,int eventSeqID) throws NotBoundException, IOException{
		String response = null;
		String cID = clientIdGen(clientIp);
		FileReader fReader=new FileReader(fileLocation);
		BufferedReader reader=new BufferedReader(fReader);
		String line=reader.readLine();
		String str=null;
		while (line !=null) {
			str +=line;
			line=reader.readLine();
		}
		byte []buffer =  str.getBytes();
		
		reader.close();
		fReader.close();
		
		reader = null;
		fReader=null;	
		String url = "rmi://"+ip+"/"+serviceName;
		DispInterface di = (DispInterface) Naming.lookup(url);
		response= di.uploadEventsToDispatcher(buffer,cID,eventSeqID);      
        if(response!=null)
        	System.out.println("Dispatcher Recieved the file from the client and the response is "+response);
		else {
			System.out.println("File sending error reported.");
		}
	}
	public void uploadTriggersFile(String ip,String serviceName,String fileLocation,String clientIp,int triggerSeqID) throws NotBoundException, IOException{
		String response = null;
		String cID = clientIdGen(clientIp); 
		FileReader fReader=new FileReader(fileLocation);
		BufferedReader reader=new BufferedReader(fReader);
		String line=reader.readLine();
		String str=null;
		while (line !=null) {
			str +=line;
			line=reader.readLine();
		}
		byte []buffer =  str.getBytes();
		
		reader.close();
		fReader.close();
		reader = null;
		fReader=null;	
		String url = "rmi://"+ip+"/"+serviceName;
		DispInterface di = (DispInterface) Naming.lookup(url);
		response= di.uploadTriggersToDispatcher(buffer,cID,triggerSeqID);
		
		
        if(response!=null)
        	System.out.println("Dispatcher Recieved the file from the client and the response is "+response);
        else
        	System.out.println("File sending error reported.");
	}
	public void regForCallback(String ip,String serviceName) throws NotBoundException, RemoteException, MalformedURLException{
		
		String url = "rmi://"+ip+"/"+serviceName;
		DispInterface di = (DispInterface) Naming.lookup(url);
		ClientCallbackInterface obj = new ClientCallbackImpl();
		di.registerCallback(obj);
		setClientObject(obj);
	}
	public void unregisterCallback(String ip,String serviceName) throws MalformedURLException, RemoteException, NotBoundException{
		String url = "rmi://"+ip+"/"+serviceName;
		DispInterface di = (DispInterface) Naming.lookup(url);
		di.unregisterCallback((ClientCallbackInterface) getclientObject());
	}
	public static String clientIdGen(String addr) {
        String[] addrArray = addr.split("\\.");
        String temp="";
        String value="";
        for (int i=0;i<addrArray.length;i++) {
        	temp=addrArray[i].toString();
        	while(temp.length()!=3){
        		temp = '0'+temp;
        	}
        	value+=temp;
        }
        return value;
    }
	public void setClientObject(Object objClient){
		obj = (ClientCallbackInterface) objClient;
	}
	public Object getclientObject(){
		return obj;
	}
//	public static void main(String[] args) throws NotBoundException, IOException {
//	ClientHandler myClient = new ClientHandler();
//	}

}
