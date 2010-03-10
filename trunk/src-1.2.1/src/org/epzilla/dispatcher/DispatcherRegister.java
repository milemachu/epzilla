package org.epzilla.dispatcher;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import org.epzilla.nameserver.NameService;
import org.epzilla.nameserver.NameServiceHandler;
import org.epzilla.dispatcher.controlers.*;
public class DispatcherRegister{

	public DispatcherRegister(){
	}
	
	public void register(String ip,String serviceName,String port,String dispatcherName) throws MalformedURLException, RemoteException, NotBoundException, UnknownHostException{
    		String url = "rmi://"+ip+"/"+serviceName;
			NameService service = (NameService)Naming.lookup(url);
        	InetAddress inetAddress = InetAddress.getLocalHost();
        	String ipAddress = inetAddress.getHostAddress();
        	int num = Integer.parseInt(port);
        	String id = dispIdGen(ipAddress);
        	String name = dispatcherName+id;
        	int i = service.insertNode(name, ipAddress, num);
        	       	if(i==1)
        	       		DispatcherUIController.appendResults("Dispatcher Successfully Registered in the Name Server");
        	       	else
        	       		DispatcherUIController.appendResults("Dispatcher Registration failure");

    }
	/*
	 * generate dispatcher id
	 */
	public static String dispIdGen(String addr) {
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

    private boolean validate(String ip) throws IOException{
    	boolean status = false;	
			status = InetAddress.getByName(ip).isReachable(3000);
			return status;
    }
//    public static void main(String args[]) {
//    	DispatcherRegister reg =new DispatcherRegister();
//    	try {
//			reg.register("127.0.0.1","NameServer","5000","Dispatcher");
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (RemoteException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NotBoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }
}
