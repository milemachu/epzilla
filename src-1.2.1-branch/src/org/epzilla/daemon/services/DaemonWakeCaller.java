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

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * This class contains the logic to wake up a lseeping node.
 * @author Harshana Eranga Martin
 *
 */
public class DaemonWakeCaller {
	private static String SERVICE_NAME="DAEMON_SERVICE";
	
	public boolean callWake(String remoteIp){
		try {
			DaemonInterface di=(DaemonInterface) Naming.lookup("rmi://"+remoteIp+"/"+SERVICE_NAME);
			return(di.wakeEpZilla());			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}	
		return false;
	}
	/**
	 * @param args
	 * @throws NotBoundException 
	 * @throws RemoteException 
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		// TODO Auto-generated method stub
		DaemonInterface di = (DaemonInterface) Naming.lookup("rmi://127.0.0.1/DAEMON_SERVICE");
		di.wakeEpZilla();
	}

}
