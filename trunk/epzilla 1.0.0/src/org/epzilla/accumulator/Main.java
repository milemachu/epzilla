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
package org.epzilla.accumulator;


import org.epzilla.accumulator.service.AccumulatorService;
import org.epzilla.accumulator.service.AccumulatorServiceImpl;
import org.epzilla.accumulator.stm.AccumulatorAsClient;
import org.epzilla.accumulator.stm.AccumulatorAsServer;
import org.epzilla.accumulator.userinterface.AccumulatorUIControler;
import org.epzilla.accumulator.util.OpenSecurityManager;
import org.epzilla.util.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.Naming;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Feb 28, 2010
 * Time: 8:40:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    private static boolean isServer = true;
    private static String SERVICE_NAME = "ACCUMULATOR_SERVICE";
    private static int SLEEP_TIME = 1000;

    private static void startRegistry() {
        try {
            Runtime.getRuntime().exec("rmiregistry");
            Thread.sleep(SLEEP_TIME);
        }
        catch (IOException ex) {
            Logger.error("", ex);
        }
        catch (InterruptedException exc) {
            Logger.error("", exc);
        }
    }

    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new OpenSecurityManager());
        }

        startRegistry();
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            String ipAddress = inetAddress.getHostAddress();
            String url = "rmi://" + ipAddress + "/" + SERVICE_NAME;
            AccumulatorService obj = new AccumulatorServiceImpl();
            Naming.rebind(url, obj);
            Logger.log("Accumulator Service successfully deployed");
        } catch (Exception e) {
            Logger.error("", e);
        }

        AccumulatorUIControler.InitializeUI();
        if (isServer) {
            AccumulatorAsServer.startServer();
            AccumulatorAsServer.loadIPList();
        } else {
            AccumulatorAsClient.startClient();
        }

    }
}
