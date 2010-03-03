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
	 public void downloadFile() {
		 try {
	         String name = "rmi://127.0.0.1/DispatcherService";
	         DispInterface di = (DispInterface) Naming.lookup(name);
	         byte[] filedata = di.downloadFileFromServer("C:\\Test.txt");
	         File file = new File("ServertoClient.txt");
	         BufferedOutputStream output = new
	           BufferedOutputStream(new FileOutputStream(file.getName()));
	         output.write(filedata,0,filedata.length);
	         output.flush();
	         output.close();
	      } catch(Exception e) {
	         System.err.println("FileServer exception: "+ e.getMessage());
	         e.printStackTrace();
	      }
	}	
	public void uploadEventsFile(String ip,String serviceName,String fileLocation,String clientIp,int eventSeqID) throws NotBoundException, IOException{
		String response = null;
		int cID = clientIdGen(clientIp);
//		byte[] cId = cID.getBytes();
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
        else
        	System.out.println("File sending error reported.");
	}
	public void uploadTriggersFile(String ip,String serviceName,String fileLocation,String clientIp,int triggerSeqID) throws NotBoundException, IOException{
		String response = null;
		int cID = clientIdGen(clientIp); 
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

	public static int clientIdGen(String addr) {
        String[] addrArray = addr.split("\\.");
        int num = 0;
        String value="";
        for (int i=1;i<addrArray.length;i++) {
//            num+=Integer.parseInt(addrArray[i]);
        	value+=addrArray[i];
        }
        num=Integer.parseInt(value);
        return num;
    }
	public static void main(String[] args) throws NotBoundException, IOException {
	ClientHandler myClient = new ClientHandler();
//	myClient.uploadFile("127.0.0.1","Dispatcher","C:\\Test.txt");
//	myClient.getServiceIp("127.0.0.1", "NameServer");
	long l=myClient.clientIdGen("190.108.34.154");
	System.out.println(l);
	}

}
