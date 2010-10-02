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
package org.epzilla.clusterNode.nodeControler;

import org.epzilla.clusterNode.leaderReg.ClusterStartup;
import org.epzilla.clusterNode.leaderReg.LeaderRegister;
import org.epzilla.clusterNode.rmi.ClusterInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * This class is to send Events to the Nodes in the Cluster
 * Author: Chathura
 * Date: May 7, 2010
 * Time: 9:45:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventSender {
    private static ClusterInterface clusterObj;
    private static String response = null;
    private static final Hashtable<String, Object> nodesList = new Hashtable<String, Object>();
    private static String SERVICE_NAME = "CLUSTER_NODE";

    public EventSender() {
    }

    public static void sendEvents(String serverIp, String event) throws RemoteException, MalformedURLException, NotBoundException {
        try {
            if ((serverIp != null) && (!nodesList.containsKey(serverIp))) {
                initNode(serverIp, SERVICE_NAME);
                clusterObj = (ClusterInterface) nodesList.get(serverIp);
                clusterObj.addEventStream(event);
                System.out.println("calling add event stream.");
                //            if (response != null) {
                //                Logger.log("Events added to the Node " + serverIp);
                //            } else {
                //                Logger.error("Events adding failure to the Node" + serverIp, null);
                //            }
            } else {
                if (serverIp == null) {
                    serverIp = "127.0.0.1";
                }                     
                clusterObj = (ClusterInterface) nodesList.get(serverIp);
                clusterObj.addEventStream(event);
                System.out.println("else part working.");

                //            if (response != null) {
                //                Logger.log("Events added to the Node " + serverIp);
                //            } else {
                //                Logger.error("Events adding failure to the Node" + serverIp, null);
                //            }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initNode(String serverIp, String serviceName) throws MalformedURLException, NotBoundException, RemoteException {
        String url = "rmi://" + serverIp + "/" + serviceName;
        ClusterInterface obj = (ClusterInterface) Naming.lookup(url);
        setClusterObject(obj);
        nodesList.put(serverIp, obj);

    }

    private static void setClusterObject(Object obj) {
        clusterObj = (ClusterInterface) obj;
    }

}
