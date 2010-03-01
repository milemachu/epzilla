package org.epzilla.dispatcher;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

public class DispatcherService {

	/**
	 * 01-03-10
	 */
	public void bindDispatcher(String serviceName) throws RemoteException, UnknownHostException, MalformedURLException {
		if(System.getSecurityManager()==null){
			System.setSecurityManager(new RMISecurityManager());
		}
		DispInterface dispInt=new DispImp();	
		InetAddress inetAddress;
		inetAddress = InetAddress.getLocalHost();
    	String ipAddress = inetAddress.getHostAddress();
    	String name ="rmi://"+ipAddress+"/"+serviceName;
		Naming.rebind(name, dispInt);
		System.out.println("Dispatcher Service successfully deployed.....");
		
	}
	public static void main(String[] args) {
		DispatcherService service =new DispatcherService();
    	try {
			service.bindDispatcher("Dispatcher");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
