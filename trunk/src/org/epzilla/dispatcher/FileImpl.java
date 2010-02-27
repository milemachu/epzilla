package org.epzilla.dispatcher;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class FileImpl extends UnicastRemoteObject implements FileInterface {

	
	protected FileImpl() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}


	public byte[] downloadFileFromServer(String fileName)
			throws IOException {
		FileReader fReader=new FileReader(fileName);
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
//		file=null;
		return buffer;
	}

	
	public String uploadFileToServer(byte[] stream) throws RemoteException {
 try {
	         
//	         File file = new File("C:\\help\\TestFromClientToServer.txt");
//	         BufferedOutputStream output = new
//	           BufferedOutputStream(new FileOutputStream(file.getName()));
//	         output.write(stream,0,stream.length);
//	         
	         
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

}
