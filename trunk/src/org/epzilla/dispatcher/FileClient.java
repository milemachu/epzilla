package org.epzilla.dispatcher;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class FileClient {
	
	public void downloadFile() {
		 try {
	         String name = "rmi://127.0.0.1/FileService";
	         FileInterface fi = (FileInterface) Naming.lookup(name);
	         byte[] filedata = fi.downloadFileFromServer("C:\\Test.txt");
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
	
	
	public void uploadFile() throws NotBoundException, IOException{
		FileReader fReader=new FileReader("C:\\Test.txt");
//		BufferedReader reader=new BufferedReader(fReader);
		
//		File file=new File(fileName);
//		byte []buffer=new byte[(int) file.length()];
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
		
		
		 String name = "rmi://127.0.0.1/FileService";
         FileInterface fi = (FileInterface) Naming.lookup(name);
         
        String response= fi.uploadFileToServer(buffer);
        
        if(response!=null)
        	System.out.println("Server Recieved the file from the client and the response is "+response);
        else
        	System.out.println("File sending error reported.");
	}
	
	public static void main(String[] args) throws NotBoundException, IOException {
		FileClient client=new FileClient();
		client.downloadFile();
		
		System.out.println("File downloaded...");
		
		client.uploadFile();
		
		System.out.println("File uploaded...");
	}

}
