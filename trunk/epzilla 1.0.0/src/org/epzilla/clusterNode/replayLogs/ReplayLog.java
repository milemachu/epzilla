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
package org.epzilla.clusterNode.replayLogs;

import org.epzilla.clusterNode.xml.ClusterSettingsReader;
import org.epzilla.dispatcher.rmi.DispInterface;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * This class is to replay the log files as requested by the Node Leader
 * consider this as a future enhancement.
 * Author: Chathura
 * Date: Apr 20, 2010
 * Time: 11:13:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReplayLog {
    private static ClusterSettingsReader reader = new ClusterSettingsReader();
    private static String clusterID = "";
    private static String SERVICE_IP = "127.0.0.1";
    private static String SERVICE_NAME = "Dispatcher127000000001";

    private static void replayLog() throws RemoteException, MalformedURLException, NotBoundException, UnknownHostException {
        loadSettings();
        String url = "rmi://" + SERVICE_IP + "/" + SERVICE_NAME;
        DispInterface service;
        service = (DispInterface) Naming.lookup(url);
        InetAddress inetAddress = InetAddress.getLocalHost();
        String ipAddress = inetAddress.getHostAddress();
        service.replayLogs(clusterID, ipAddress);
    }

    private static void loadSettings() {
        try {
            ArrayList<String[]> data = reader.getServerIPSettings("clusterID_settings.xml");
            String[] ar = data.get(0);
            clusterID = ar[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
