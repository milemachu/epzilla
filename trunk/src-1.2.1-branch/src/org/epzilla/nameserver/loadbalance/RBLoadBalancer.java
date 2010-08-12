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

import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * This class is use to balance the Dispatcher load in a Round Robin way
 * it returns the Dispatcher IP which has the lowest weight among the available Dispatchers
 * Author: Chathura
 * Date: Mar 29, 2010
 * Time: 6:00:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class RBLoadBalancer {
    private static Hashtable<String, Integer> ipTable = new Hashtable<String, Integer>();
    private static int loadFactor = 0;
    private static IpComparator myComparator = new IpComparator();
    private static Thread runner;

    /*
   initially adding dispatcer data
    */

    public static void insert(String ipAddress) {
        ipTable.put(ipAddress, 0);
    }
    /*
   increment the load of particular dispatcher
    */

    public static void updateInc(String ipAddress) {
        if (ipTable.containsKey(ipAddress)) {
            loadFactor = ipTable.get(ipAddress);
            loadFactor++;
        }
        ipTable.remove(ipAddress);
        ipTable.put(ipAddress, loadFactor);
    }
    /*
   decrement the load of particular dispathcer
    */

    public static void updateDec(String ipAddress) {
        if (ipTable.containsKey(ipAddress)) {
            loadFactor = ipTable.get(ipAddress);
            loadFactor--;
        }
        ipTable.remove(ipAddress);
        ipTable.put(ipAddress, loadFactor);
    }
    /*
    get the least loaded dispatcher IP from the existing data
     */

    public static String getIPAddress() {
        Object[] myArray = ipTable.entrySet().toArray();
        Arrays.sort(myArray, myComparator);
        return (String) ((Map.Entry) myArray[0]).getKey();
    }
}
/*
compare the load of two IPs and return the least loaded IP
*/

class IpComparator implements Comparator {
    public int compare(Object o1, Object o2) {
        if ((Integer) ((Map.Entry) o1).getValue() > (Integer) ((Map.Entry) o2).getValue()) {
            return (1);
        } else if ((Integer) ((Map.Entry) o1).getValue() < (Integer) ((Map.Entry) o2).getValue()) {
            return (-1);
        } else {
            return (0);
        }
    }
}
