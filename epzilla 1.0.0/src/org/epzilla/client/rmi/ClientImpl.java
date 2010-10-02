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

import org.epzilla.client.controlers.ClientUIControler;
import org.epzilla.util.Logger;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * Implementaion of the ClientImpl interface
 * Author: Chathura
 * Date: May 3, 2010
 * Time: 4:20:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientImpl extends UnicastRemoteObject implements ClientInterface {


    public ClientImpl() throws RemoteException {
    }

    /*
   accept alert messages
    */

    Hashtable<String, String> duplicateMap = new Hashtable();

    public String notifyClient(byte[] notifications, byte[] eventId) {
        try {
            String alert = new String(notifications);
            String eid = new String(eventId);

            if (duplicateMap.get(eid) == null) {
                ClientUIControler.appendAlerts(alert);
                duplicateMap.put(eid, alert);
            } else {
                duplicateMap.remove(eid);
            }

            // todo - drop duplicates
            return "OK";
        } catch (Exception ex) {
            Logger.error("", ex);
        }
        return null;
    }
}
