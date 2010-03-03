package org.epzilla.dispatcher;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import org.epzilla.nameserver.NameService;
import org.epzilla.nameserver.NameServiceHandler;

public class DispatcherRegister{

	public DispatcherRegister(){
	}
	
	public void register(String ip,String serviceName,String dispatcherName) throws MalformedURLException, RemoteException, NotBoundException, UnknownHostException{
    		String url = "rmi://"+ip+"/"+serviceName;
			NameService service = (NameService)Naming.lookup(url);
        	InetAddress inetAddress = InetAddress.getLocalHost();
        	String ipAddress = inetAddress.getHostAddress();
        	int id = dispIdGen(ipAddress);
        	String name = dispatcherName+id;
        	int i = service.insertNode(name, ipAddress, 5005);
        	       	if(i==0)
        	       		System.out.println("Insertion failure");
        	       	else if(i==1)
        	       		System.out.println("Successfully inserted");        
    }
	/*
	 * generate dispatcher id
	 */
	public static int dispIdGen(String addr) {
        String[] addrArray = addr.split("\\.");
        int num = 0;
        String value="";
        for (int i=1;i<addrArray.length;i++) {
//            num+=Integer.parseInt(addrArray[i]);
        	value+=addrArray[i];
        }
        num=Integer.parseInt(value);
        return num;
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
