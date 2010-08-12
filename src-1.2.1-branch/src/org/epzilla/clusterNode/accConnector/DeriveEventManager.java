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

import org.epzilla.util.Logger;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * This class is use to send the Derived events
 * Author: Chathura
 * Date: May 19, 2010
 * Time: 8:35:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class DeriveEventManager {
    private static ArrayList<String> ipArr = new ArrayList<String>();
    private static boolean isLoad = false;

    public static void dispatchEvents(String event) {
        if (!isLoad) {
            loadAccumulatorIPs();
        }
        byte[] bteEvent = event.getBytes();

        try {
            DeriveEventSender.sendDeriveEvent(ipArr.get(0), bteEvent);
        } catch (MalformedURLException e) {
            Logger.error("", e);
        } catch (NotBoundException e) {
            Logger.error("", e);
        } catch (RemoteException e) {
            Logger.error("", e);
        }
    }

    public static void loadAccumulatorIPs() {
        isLoad = true;
    }
}
