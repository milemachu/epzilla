package org.epzilla.ui.rmi;

import java.rmi.*;
import java.rmi.server.*;

import org.epzilla.ui.controlers.*;

public class ClientCallbackImpl extends UnicastRemoteObject implements ClientCallbackInterface {
	
	ClientUIControler clientCon;
	
	public ClientCallbackImpl() throws RemoteException {
      super( );
   }
	
   public void notifyClient(String message){
      clientCon = new ClientUIControler(message);
      Thread t = new Thread(clientCon);
      t.start();
   }      
}
