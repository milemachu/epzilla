package org.epzilla.ui;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.Vector;
import org.epzilla.nameserver.*;
import org.epzilla.dispatcher.*;

public class ClientHandler {
	
	InetAddress dispatcher;
	int dispatcherPort;
	Vector<String> dispArray=new Vector<String>();
	String ip ="";
	
	 public Vector<String> getService(String serverIp,String serviceName){
	    	
	       	try {
	       		String url = "rmi://"+serverIp+"/"+serviceName;
	       		NameService r = (NameService)Naming.lookup(url);
	    		int size = r.getDirectorySize();
				for(int i=0; i<size;i++){
				String ip = r.getHostName(i);
				System.out.println(ip);
				dispArray.add(ip);
				
				}
	       	}
			catch (IOException e) {
				e.printStackTrace();
			} catch (NotBoundException e) {
				e.printStackTrace();
			}
			return dispArray;
	    }
	 public String getHost(String disIp,String serviceName){
	    	
	       	try {
	       		String url = "rmi://"+disIp+"/"+serviceName;
	       		NameService r = (NameService)Naming.lookup(url);
	    		int size = r.getDirectorySize();
				for(int i=0; i<size;i++){
				ip = r.getHostName(i);
				System.out.println(ip);
				}
	       	}
			catch (IOException e) {
				e.printStackTrace();
			} catch (NotBoundException e) {
				e.printStackTrace();
			}
			return ip;
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
	public void uploadFile(String ip,String serviceName,String fileLocation) throws NotBoundException, IOException{
		FileReader fReader=new FileReader(fileLocation);

		BufferedReader reader=new BufferedReader(fReader);
		String line=reader.readLine();
		String str=null;
		while (line !=null) {
			str +=line;
			line=reader.readLine();
		}
		
		byte []buffer=str.getBytes();
		
		reader.close();
		fReader.close();
		
		reader = null;
		fReader=null;
		
		
		 String url = "rmi://"+ip+"/"+serviceName;
		 DispInterface di = (DispInterface) Naming.lookup(url);
         
        String response= di.uploadFileToDispatcher(buffer);
        
        if(response!=null)
        	System.out.println("Dispatcher Recieved the file from the client and the response is "+response);
        else
        	System.out.println("File sending error reported.");
	}
	
	public static void main(String[] args) throws NotBoundException, IOException {
	ClientHandler myClient = new ClientHandler();
//	String host = myClient.getHost("127.0.0.1","NameServer");
//	myClient.uploadFile(host,"NameServer");
	myClient.getService("10.8.108.151", "NameServer");
	}

}
