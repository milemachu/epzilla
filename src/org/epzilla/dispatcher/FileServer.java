package org.epzilla.dispatcher;

import org.epzilla.dispatcher.FileInterface;
import org.epzilla.dispatcher.FileImpl;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;

public class FileServer {
	
	public static void main(String[] args) {
		if(System.getSecurityManager()==null){
			System.setSecurityManager(new RMISecurityManager());
		}
		
		try {
			FileInterface fileInt=new FileImpl();
			Naming.rebind("rmi://127.0.0.1/FileService", fileInt);
			
			System.out.println("File Service successfully deployed.....");
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
