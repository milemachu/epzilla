package org.epzilla.dispatcher;

import java.awt.Toolkit;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;


import javax.swing.JTextArea;

public class EventListener {
	DispatcherRegister disReg = new DispatcherRegister();
	Timer timer;
	Toolkit toolkit;
	public EventListener(){
	}
	public void register(String ip,String serviceName,String dispatcherName) throws MalformedURLException, RemoteException, UnknownHostException, NotBoundException{
		disReg.register(ip,serviceName,dispatcherName);
	}
	public void bindDispatcher(String disServiceName) throws RemoteException, UnknownHostException, MalformedURLException{
		disReg.bindDispatcher(disServiceName);
	}
	public void getTriggers(JTextArea jt){
		jt.append("fxvgfd");
	}
	
}
