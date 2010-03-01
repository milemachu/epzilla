package org.epzilla.dispatcher;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import org.epzilla.nameserver.NameService;
import org.epzilla.nameserver.NameServiceHandler;

public class DispatcherRegister{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String urlName = "NameServer";

	public DispatcherRegister(){
	}
	
	public void register(String ip,String serviceName,String dispatcherName) throws MalformedURLException, RemoteException, NotBoundException, UnknownHostException{
    		String url = "rmi://"+ip+"/"+serviceName;
			NameService service = (NameService)Naming.lookup(url);
        	InetAddress inetAddress = InetAddress.getLocalHost();
        	String ipAddress = inetAddress.getHostAddress();
        	String name = dispatcherName;
        	int i = service.insertNode(name, ipAddress, 5005);
        	       	if(i==0)
        	       		System.out.println("Insertion failure");
        	       	else if(i==1)
        	       		System.out.println("Successfully inserted");        
    }
    public static void main(String args[]) {
    	DispatcherRegister reg =new DispatcherRegister();
    	try {
			reg.register("127.0.0.1","NameServer","Dispatcher");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
