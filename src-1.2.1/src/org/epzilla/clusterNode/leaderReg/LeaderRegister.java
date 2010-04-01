package org.epzilla.clusterNode.leaderReg;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.epzilla.dispatcher.rmi.DispInterface;
import org.epzilla.dispatcher.rmi.OpenSecurityManager;
import org.epzilla.clusterNode.rmi.ClusterInterface;
import org.epzilla.clusterNode.rmi.ClusterImpl;
import org.epzilla.clusterNode.NodeController;

public class LeaderRegister {

	public void bindClusterNode(String serviceName) throws UnknownHostException, MalformedURLException, RemoteException {
        if(System.getSecurityManager()==null){
			System.setSecurityManager(new OpenSecurityManager());
		}
		ClusterInterface clusterInt=new ClusterImpl();
		InetAddress inetAddress;
		inetAddress = InetAddress.getLocalHost();                           
    	String ipAddress = inetAddress.getHostAddress();
        String sname= serviceName;
    	String name ="rmi://"+ipAddress+"/"+sname;
		Naming.rebind(name, clusterInt);
		System.out.println("Cluster Node successfully deployed.....");
    }
    private void register() throws RemoteException, MalformedURLException, NotBoundException, UnknownHostException {
        String url = "rmi://"+"127.0.0.1"+"/"+"Dispatcher127000000001";
		DispInterface service;
		service = (DispInterface)Naming.lookup(url);
		InetAddress inetAddress = InetAddress.getLocalHost();
	    String ipAddress = inetAddress.getHostAddress();
	    service.acceptLeaderIp(ipAddress);
    }
	public static void main(String[] args) {
       LeaderRegister leader = new LeaderRegister();
        try {
            NodeController.init();
            leader.bindClusterNode("Cluster");
            leader.register();
        } catch (UnknownHostException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NotBoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
//        catch (NotBoundException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }

    }

}
