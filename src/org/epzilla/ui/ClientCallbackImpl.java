package org.epzilla.ui;

import java.rmi.*;
import java.rmi.server.*;

public class ClientCallbackImpl extends UnicastRemoteObject implements ClientCallbackInterface {
	
	ClientUIControler cui;
	
	public ClientCallbackImpl() throws RemoteException {
      super( );
   }
	
   public void notifyClient(String message){
      cui = new ClientUIControler(message);
      Thread t = new Thread(cui);
      t.start();
   }      

}
