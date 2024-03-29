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
package org.epzilla.clusterNode.accConnector;

import org.epzilla.accumulator.service.AccumulatorService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * This class is to send the Derive Events to the Accumulator
 * Author: Chathura
 * Date: May 15, 2010
 * Time: 7:15:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class DeriveEventSender {
    private static AccumulatorService accObj;
    private static String response = null;
    private static Hashtable<String, Object> accList = new Hashtable<String, Object>();
    private static String SERVICE_NAME = "ACCUMULATOR_SERVICE";

    public DeriveEventSender() {
    }
    /*
    * method send the derive events to the accumulator
    */

    public static void sendDeriveEvent(String serverIP, byte[] deriveEvent) throws MalformedURLException, NotBoundException, RemoteException {

        if (!accList.containsKey(serverIP)) {
            initAccumulator(serverIP, SERVICE_NAME);
            accObj.receiveDeriveEvent(deriveEvent);
        } else {
            accObj = (AccumulatorService) accList.get(serverIP);
            accObj.receiveDeriveEvent(deriveEvent);
        }

    }
    /*
    * metod initialize the remote reference to the accumulator
    */

    private static void initAccumulator(String serverIP, String serviceName) throws MalformedURLException, NotBoundException, RemoteException {
        String url = "rmi://" + serverIP + "/" + serviceName;
        AccumulatorService obj = (AccumulatorService) Naming.lookup(url);
        setAccObject(obj);
        accList.put(serverIP, getAccObject());

    }

    private static void setAccObject(Object obj) {
        accObj = (AccumulatorService) obj;
    }

    private static Object getAccObject() {
        return accObj;
    }
}
