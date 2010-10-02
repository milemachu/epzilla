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
package org.epzilla.client.rmi;

import org.epzilla.client.userInterface.SplashScreen;
import org.epzilla.util.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.Naming;

/**
 * Created by IntelliJ IDEA.
 * This class is use as the startup class of the Cliet
 * Author: Chathura
 * Date: May 3, 2010
 * Time: 4:23:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientStartup {
    private static String serviceName = "CLIENT";
    private static int SPALSH_TIME = 3000;
    private static int SLEEP_TIME = 1000;
    static ClientInterface obj;
    /*
    Bind the Client to its RMI registry
     */

    public static void bindClient() {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new org.epzilla.client.rmi.OpenSecurityManager());
        }
        startRegistry();
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            String ipAddress = inetAddress.getHostAddress();
            String url = "rmi://" + ipAddress + "/" + serviceName;
            obj = new ClientImpl();
            Naming.rebind(url, obj);
            Logger.log("Client successfully deployed");
        } catch (Exception e) {
            Logger.error("Client Start up:", e);
        }
    }
    /*
    Start RMI registry in the client
     */

    private static void startRegistry() {
        try {
            Runtime.getRuntime().exec("rmiregistry");
            Thread.sleep(SLEEP_TIME);
        }
        catch (IOException ex) {
            Logger.error("RMI registry start:", ex);
        }
        catch (InterruptedException exc) {
            Logger.error("RMI registry start: ", exc);
        }
    }

    public static void main(String[] args) {
        bindClient();
        SplashScreen sc = new SplashScreen(SPALSH_TIME);
        sc.showSplashAndExit();
    }
}
