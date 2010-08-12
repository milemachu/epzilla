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
package org.epzilla.nameserver;


import org.epzilla.util.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by IntelliJ IDEA.
 * This is Startup class of the Name Server
 * Author: Chathura
 * To change this template use File | Settings | File Templates.
 */
public class NameServiceHandler extends UnicastRemoteObject {
    private static String serviceName = "NAME_SERVICE";

    public NameServiceHandler() throws RemoteException {
    }
    /*
   start RMI registry
    */

    private void startRegistry() {
        try {
            Runtime.getRuntime().exec("rmiregistry");
            Thread.sleep(1000);
        }
        catch (IOException ex) {
            Logger.error("IO error:", ex);
        }
        catch (InterruptedException exc) {
            Logger.error("Interruption error:", exc);
        }
    }
    /*
    bind the name server to its registry
     */

    public void bind(String serviceName) {

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new org.epzilla.dispatcher.rmi.OpenSecurityManager());
        }
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            String ipAddress = inetAddress.getHostAddress();
            String url = "rmi://" + ipAddress + "/" + serviceName;
            NameService obj = new NameServiceImpl();
            Naming.rebind(url, obj);
            Logger.log("NameServer successfully deployed");
        } catch (Exception e) {
            Logger.log("NameService err: " + e.getMessage());
        }

    }

    public static void main(String args[]) throws RemoteException {
        NameServiceHandler handler = new NameServiceHandler();
        handler.startRegistry();
        handler.bind(serviceName);

    }


}

