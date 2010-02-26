package org.epzilla.dispatcher;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

public class EventListener {
	DispatcherRegister disReg = new DispatcherRegister();
	public EventListener(){
	}
	public void register(String ip,String serviceName){
		disReg.register(ip,serviceName);
	}
	public void bindDispatcher(String disServiceName) throws RemoteException, UnknownHostException, MalformedURLException{
		disReg.bindDispatcher(disServiceName);
	}
}
