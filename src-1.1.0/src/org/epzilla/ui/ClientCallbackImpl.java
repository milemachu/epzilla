package org.epzilla.ui;

import java.rmi.*;
import java.rmi.server.*;

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
