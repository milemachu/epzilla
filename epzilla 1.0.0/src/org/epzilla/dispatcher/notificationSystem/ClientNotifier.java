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
package org.epzilla.dispatcher.notificationSystem;

import org.epzilla.client.rmi.ClientInterface;
import org.epzilla.util.Logger;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * This class use to send accumulated notifications to the client machine.
 * Author: Chathura
 * Date: May 3, 2010
 * Time: 3:27:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientNotifier {
    private static HashMap clientMap = new HashMap<String, String>();
    private static String response = null;
    private static String SERVICE_NAME = "CLIENT";

//    public static void getNotifications(String serverIp, String notifications) throws MalformedURLException, NotBoundException, RemoteException {
//        byte[] msg = notifications.getBytes();
//
//        if (clientMap.containsKey(serverIp)) {
//            ClientInterface clientObj = (ClientInterface) clientMap.get(serverIp);
//            response = clientObj.notifyClient(msg);
//            if (response != null)
//                Logger.log("Notifications send to the client");
//            else
//                Logger.log("Notifications not sent");
//        } else {
//            ClientInterface clientObj = initClient(serverIp, SERVICE_NAME);
//            response = clientObj.notifyClient(msg);
//            if (response != null)
//                Logger.log("Notifications send to the client");
//            else
//                Logger.log("Notifications not sent");
//        }
//    }
//
//    private static ClientInterface initClient(String serverIp, String serviceName) throws MalformedURLException, NotBoundException, RemoteException {
//        String url = "rmi://" + serverIp + "/" + serviceName;
//        ClientInterface obj = (ClientInterface) Naming.lookup(url);
//        clientMap.put(serverIp, obj);
//        return obj;
//
//    }

}
//
