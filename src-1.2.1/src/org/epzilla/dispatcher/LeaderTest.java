package org.epzilla.dispatcher;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.epzilla.dispatcher.controlers.DispatcherUIController;
import org.epzilla.dispatcher.rmi.DispInterface;
import org.epzilla.nameserver.NameService;

public class LeaderTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String url = "rmi://"+"10.8.108.170"+"/"+"Dispatcher010008108170";
		DispInterface service;
		try {
			service = (DispInterface)Naming.lookup(url);
			InetAddress inetAddress = InetAddress.getLocalHost();
	    	String ipAddress = inetAddress.getHostAddress();
	    	service.acceptLeaderIp(ipAddress);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	    	

	}

}
