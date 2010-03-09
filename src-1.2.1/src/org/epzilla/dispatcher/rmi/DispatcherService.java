package org.epzilla.dispatcher.rmi;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

import org.epzilla.dispatcher.controlers.*;

public class DispatcherService {

	public void bindDispatcher(String serviceName) throws RemoteException, UnknownHostException, MalformedURLException {
		if(System.getSecurityManager()==null){
			System.setSecurityManager(new OpenSecurityManager());
		}
		DispInterface dispInt=new DispImpl();	
		InetAddress inetAddress;
		inetAddress = InetAddress.getLocalHost();
    	String ipAddress = inetAddress.getHostAddress();
    	String id = dispIdGen(ipAddress);
    	String disServiceName = serviceName+id;
    	String name ="rmi://"+ipAddress+"/"+disServiceName;
		Naming.rebind(name, dispInt);
		System.out.println("Dispatcher Service successfully deployed.....");
		
	}
	/*
	 * generate dispatcher id
	 */
	public String dispIdGen(String addr) {
        String[] addrArray = addr.split("\\.");
        String temp="";
        String value="";
        for (int i=0;i<addrArray.length;i++) {
        	temp=addrArray[i].toString();
        	while(temp.length()!=3){
        		temp = '0'+temp;
        	}
        	value+=temp;
        }
        return value;
    }
	public static void main(String[] args) {
		DispatcherService service =new DispatcherService();
    	try {
			service.bindDispatcher("Dispatcher");
            MainDispatcherController.run();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

}
