package org.epzilla.dispatcher;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

import org.epzilla.ui.ClientCallbackInterface;
import org.epzilla.dispatcher.dataManager.TriggerManager;

public class DispImpl extends UnicastRemoteObject implements DispInterface {

	private Vector<ClientCallbackInterface> clientList = new Vector<ClientCallbackInterface>();

    protected DispImpl() throws RemoteException {
        super();
        clientList = new Vector();

    }
    public String uploadEventsToDispatcher(byte[] stream,String clientID,int eventSeqID) throws RemoteException {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("ClientToServer.txt"));
            writer.write(new String(stream));
            writer.flush();
            writer.close();
            System.out.println(clientID+" "+eventSeqID);
            return "Ok";
        } catch (Exception e) {
            System.err.println("FileServer exception: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public String uploadTriggersToDispatcher(byte[] stream,String clientID,int triggerSeqID) throws RemoteException {
        try {
            TriggerManager.addTriggerToList(stream);
            return "Ok";

        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
	@Override
	public String acceptNotifications() throws RemoteException {
		return "hello";
	}
	@Override
	public void registerCallback(ClientCallbackInterface callbackClientObject)throws RemoteException {
		if (!(clientList.contains(callbackClientObject))) {
	         clientList.addElement(callbackClientObject);
	      System.out.println("Registered new client ");
	      calllbacks();
		}
	}
	@Override
	public void unregisterCallback(ClientCallbackInterface callbackClientObject)throws RemoteException {
		 if (clientList.removeElement(callbackClientObject)) {
		      System.out.println("Unregistered client ");
		      calllbacks();
		    } else {
		       System.out.println(
		         "unregister: clientwasn't registered.");
		    }
	}
	public synchronized void calllbacks( ) throws RemoteException{
		ClientCallbackInterface nextClient = (ClientCallbackInterface)clientList.elementAt(1);
        nextClient.notifyClient("Events hit="+  clientList.size());
	  } 


}
