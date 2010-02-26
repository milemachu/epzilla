package org.epzilla.dispatcher;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import org.epzilla.nameserver.NameService;
import org.epzilla.nameserver.NameServiceHandler;

public class DispatcherRegister implements Remote {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String urlName = "NameServer";

	public DispatcherRegister(){
	}
	public void bindDispatcher(String serviceName) {
		try {
		DispInterface dispInt=new DispImp();	
		InetAddress inetAddress;
		inetAddress = InetAddress.getLocalHost();
    	String ipAddress = inetAddress.getHostAddress();
    	String name ="rmi://"+ipAddress+"/"+serviceName;
		Naming.rebind(name, dispInt);
		System.out.println("Dispatcher Service successfully deployed.....");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void register(String ip,String serviceName){
    	try {
    		String url = "rmi://"+ip+"/"+serviceName;
			NameService service = (NameService)Naming.lookup(url);
        	InetAddress inetAddress = InetAddress.getLocalHost();
        	String ipAddress = inetAddress.getHostAddress();
        	String name = inetAddress.getHostName();
        	int i = service.insertNode(name, ipAddress, 5005);
        	       	if(i==0)
        	       		System.out.println("Insertion failure");
        	       	else if(i==1)
        	       		System.out.println("Successfully inserted");
        	       		
            System.out.println("IP Address: "+ipAddress);
          	System.out.println("Host Name: "+name);   
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    public static void main(String args[]) {
    	DispatcherRegister reg =new DispatcherRegister();
//    	reg.bindDispatcher("Dispa");
//    	reg.register("127.0.0.1","NameServer");
    }
}
