package org.epzilla.dispatcher.rmi;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

import org.epzilla.ui.rmi.*;
import org.epzilla.dispatcher.dataManager.*;

public class DispImpl extends UnicastRemoteObject implements DispInterface {

	private Vector<ClientCallbackInterface> clientList = new Vector<ClientCallbackInterface>();
	int id;
	String clusterID="";
    protected DispImpl() throws RemoteException {
        //super();
    }
    public String uploadEventsToDispatcher(byte[] stream,String clientID,int eventSeqID) throws RemoteException {
        try {
        //add events to dispatcher logic here 
        	return "OK";
        } catch (Exception e) {
            System.err.println("FileServer exception: " + e.getMessage());
        }
        return null;
    }
    @Override
    public String uploadTriggersToDispatcher(byte[] stream,String clientID,int triggerSeqID) throws RemoteException {
        try {
            TriggerManager.addTriggerToList(stream);
            return "Ok";

        } catch (Exception e) {
        	System.err.println("FileServer exception: " + e.getMessage());
        }
        return null;
    }
	@Override
	public String acceptNotifications() throws RemoteException {
		return null;		
	}
	@Override
	public void registerCallback(ClientCallbackInterface clientObject)throws RemoteException {
		if (!(clientList.contains(clientObject))) {
	         clientList.addElement(clientObject);
	      System.out.println("Registered new client "+clientObject);
	      calllbacks();
		}
	}
	@Override
	public void unregisterCallback(ClientCallbackInterface clientObject)throws RemoteException {
		 if (clientList.removeElement(clientObject)) {
		      System.out.println("Unregistered client ");
		    } else {
		       System.out.println(
		         "unregister: clientwasn't registered."+ clientObject);
		    }
	}
	public synchronized void calllbacks( ) throws RemoteException{
		for (int i = 0; i < clientList.size(); i++){
		ClientCallbackInterface nextClient = clientList.elementAt(i);
        nextClient.notifyClient("Events hit="+  clientList.size());
	  } 
	}
	@Override
	public void acceptLeaderIp(String ip)	throws RemoteException {
		try{
			clusterID = "CID"+id;
			ClusterLeaderIpListManager.addIP(clusterID, ip);
			id++;
			}catch(Exception e){
				e.printStackTrace();
		}
	}


}
