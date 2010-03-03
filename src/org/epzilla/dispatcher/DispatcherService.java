package org.epzilla.dispatcher;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

public class DispatcherService {

	public void bindDispatcher(String serviceName) throws RemoteException, UnknownHostException, MalformedURLException {
		if(System.getSecurityManager()==null){
			System.setSecurityManager(new RMISecurityManager());
		}
		DispInterface dispInt=new DispImp();	
		InetAddress inetAddress;
		inetAddress = InetAddress.getLocalHost();
    	String ipAddress = inetAddress.getHostAddress();
    	int id = dispIdGen(ipAddress);
    	String disServiceName = serviceName+id;
    	String name ="rmi://"+ipAddress+"/"+disServiceName;
		Naming.rebind(name, dispInt);
		System.out.println("Dispatcher Service successfully deployed.....");
		
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
