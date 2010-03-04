package org.epzilla.dispatcher;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

import org.epzilla.ui.ClientCallbackInterface;

public class DispImpl extends UnicastRemoteObject implements DispInterface {

	private Vector<ClientCallbackInterface> clientList;

    protected DispImpl() throws RemoteException {
        super();
        clientList = new Vector();

    }
    public byte[] downloadFileFromServer(String fileName)
            throws IOException {
        FileReader fReader = new FileReader(fileName);
        BufferedReader reader = new BufferedReader(fReader);
        String line = reader.readLine();
        String str = null;
        while (line != null) {
            str += line;
            line = reader.readLine();
        }
        byte[] buffer = str.getBytes();
        reader.close();
        fReader.close();
        reader = null;
        fReader = null;
        return buffer;
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
		    } else {
		       System.out.println(
		         "unregister: clientwasn't registered.");
		    }
	}
	private synchronized void calllbacks( ) throws java.rmi.RemoteException{

	    for (int i = 0; i < clientList.size(); i++){
	      System.out.println("doing "+ i +"-th callback\n");    
	      ClientCallbackInterface nextClient = (ClientCallbackInterface)clientList.elementAt(i);
	        nextClient.notifyClient("Number of registered clients="+  clientList.size());
	    }
	  } 


}
