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
package org.epzilla.nameserver.loadbalance;

import org.epzilla.util.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * This class is to use the Load balancing purpose of the Dispathcer
 * Author: Chathura
 * Date: Mar 20, 2010
 * Time: 6:39:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoadBalancer {
    private static int timeOut = 2000;
    private static Hashtable<String, String> ipTable = new Hashtable<String, String>();
    private static String ipAddress = "";
    private static Random generator = new Random();
    private static int ips = 0;
    /*
   insert the dispatcher details to the hash table
    */

    private static String insertIntoTable(String clientID, String[] ipAddrs, int dirSize) {
        if (ipTable.isEmpty()) {
            ipTable.put(clientID, ipAddrs[0]);
            return ipAddrs[0];
        } else {
            int id = selectRand(dirSize);
            ipTable.put(clientID, ipAddrs[id]);
            return ipAddrs[id];
        }
    }
    /*
    search hash table for existing dispatcher information
    if not add new entry to add information
     */

    public static String search(String clientID, int dirsize, String[] ipAddrs) {
        if (ipTable.containsKey(clientID)) {
            ipAddress = ipTable.get(clientID);
        } else if (!ipTable.containsKey(clientID)) {
            ipAddress = insertIntoTable(clientID, ipAddrs, dirsize);
        }
        if (!isValidIp(ipAddress)) {
            ipTable.remove(clientID);
            System.err.println(ipAddress);
            search(clientID, dirsize, ipAddrs);
        }
        Logger.log(ipAddress);
        return ipAddress;
    }

    private static int selectRand(int dirSize) {
        int id = 1;
        id = generator.nextInt(dirSize);
        return id;
    }

    private static boolean isValidIp(String ip) {
        boolean status = false;
        try {
            status = InetAddress.getByName(ip).isReachable(timeOut);

        } catch (UnknownHostException e) {
            Logger.error("", e);
        } catch (IOException e) {
            Logger.error("", e);
        }
        return status;
    }
}
