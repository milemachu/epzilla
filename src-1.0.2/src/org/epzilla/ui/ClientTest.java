package org.epzilla.ui;

import org.epzilla.testObjectGenerator.EventTriggerGenerator;
import org.epzilla.dispatcher.*;
import org.epzilla.dispatcher.dispatcherObjectModel.TriggerInfoObject;

import java.io.FileReader;
import java.io.BufferedReader;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.util.TimerTask;

import jstm.core.Site;
import jstm.core.Transaction;

/**
 * Created by IntelliJ IDEA.
 * User: Team epZilla
 * Date: Mar 8, 2010
 * Time: 12:40:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientTest extends Thread{

    DispInterface di;
    public ClientTest(){

    }
    public void setDispatcherObj(Object obj){
         di = (DispInterface) obj;
    }
    public Object getDispatcherObj(){
         return di;
    }
    public void lookUp(String ip, String name) throws MalformedURLException, NotBoundException, RemoteException {
         String url = "rmi://"+ip+"/"+name;
		DispInterface di = (DispInterface) Naming.lookup(url);
        setDispatcherObj(di);
    }
    public void initProcess(String ip, String name) throws MalformedURLException, NotBoundException, RemoteException {
        lookUp(ip,name);
        initSendTriggerStream();

    }
    public void initSendTriggerStream() {
        final java.util.Timer timer1 = new java.util.Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
        String str  = EventTriggerGenerator.getNextTrigger();
        String cID= "1";
        int triggerSeqID = 1;
       	String response = null;

		byte []buffer =  str.getBytes();

                try {
                    response = di.uploadTriggersToDispatcher(buffer,cID,triggerSeqID);
                } catch (RemoteException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                if(response!=null)
        	System.out.println("Dispatcher Recieved the file from the client and the response is "+response);
		else {
			System.out.println("File sending error reported.");
		}

            }
        }, 0, 5000);
    }

}
