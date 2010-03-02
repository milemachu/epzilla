package org.epzilla.dispatcher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DispImp extends UnicastRemoteObject implements DispInterface {

	
	protected DispImp() throws RemoteException {
		super();
	}


	public byte[] downloadFileFromServer(String fileName)
			throws IOException {
		FileReader fReader=new FileReader(fileName);
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
		return buffer;
	}

	
	public String uploadEventsToDispatcher(byte[] stream) throws RemoteException {
		try {	         
	         BufferedWriter writer=new BufferedWriter(new FileWriter("ClientToServer.txt"));
	         writer.write(new String(stream));
	         writer.flush();
	         writer.close();
	         return "Ok";
	         
	      } catch(Exception e) {
	         System.err.println("FileServer exception: "+ e.getMessage());
	         e.printStackTrace();
	      }	
		return null;
	}


	@Override
	public String uploadTriggersToDispatcher(byte[] stream)	throws RemoteException {

		return null;
	}

}
