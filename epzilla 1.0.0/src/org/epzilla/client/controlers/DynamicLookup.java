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
package org.epzilla.client.controlers;

import org.epzilla.nameserver.NameService;
import org.epzilla.util.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.UnknownHostException;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * This class is to perform the Dynamic Lookup from the Client
 * which consider as a future enhancement to the project
 * Author: Chathura
 * Date: May 22, 2010
 * Time: 7:18:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class DynamicLookup {
    private static final int TIME_OUT = 3000;

    public static void dynamicLookup() {
        try {
            NameService service = (NameService) ClientHandler.getNameSerObj();
            int size = service.getDirectorySize();
            for (int i = 0; i < size; i++) {
                String dispData = service.getDispatcherIP();
                StringTokenizer st = new StringTokenizer(dispData);
                String ip = st.nextToken();
                if (validate(ip)) {
                    ClientUIControler.setListLookup(dispData);
                    break;
                }
            }
        } catch (RemoteException e) {
            Logger.error("", e);
        }
    }
    /*
    * check the IP is reachable
    */

    private static boolean validate(String ip) {
        boolean status;
        try {
            status = InetAddress.getByName(ip).isReachable(TIME_OUT);
            status = true;
        } catch (UnknownHostException e) {
            status = false;
        } catch (IOException e) {
            status = false;
        }
        return status;
    }
}
