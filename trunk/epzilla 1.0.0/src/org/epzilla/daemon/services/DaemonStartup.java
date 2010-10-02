/***************************************************************************************************
Copyright 2009 - 2010 Harshana Eranga Martin, Dishan Metihakwala, Rajeev Sampath, Chathura Randika

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
****************************************************************************************************/
package org.epzilla.daemon.services;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

/**
 * This is the startup sequence for the daemon service.
 * @author Harshana Eranga Martin 	 mailto: harshana05@gmail.com
 *
 */
public class DaemonStartup {
	private static String SERVICE_NAME="DAEMON_SERVICE";
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args){
		if(System.getSecurityManager()==null){
			System.setSecurityManager(new RMISecurityManager());
		}
		DaemonInterface daemon;
		try {
			daemon = new DaemonImpl();
			InetAddress inetAddress;
			inetAddress = InetAddress.getLocalHost();
	    	String ipAddress = inetAddress.getHostAddress();
	    	String name ="rmi://"+ipAddress+"/"+SERVICE_NAME;
			Naming.rebind(name, daemon);
			System.out.println("Daemon Service successfully deployed.....");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}	
		
	}

}
