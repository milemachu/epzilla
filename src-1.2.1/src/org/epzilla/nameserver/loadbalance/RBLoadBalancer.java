package org.epzilla.nameserver.loadbalance;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: chathura
 * Date: Mar 29, 2010
 * Time: 6:00:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class RBLoadBalancer {
    private static Hashtable<String, Integer> ipTable = new Hashtable<String, Integer>();
    private static int loadFactor = 0;
    private static Object[] myArray;
    private static IpComparator myComparator = new IpComparator();
    private static Thread runner;


    public static void insert(String ipAddress) {
        ipTable.put(ipAddress, 0);
    }

    public static void update(String ipAddress) {
        if (ipTable.containsKey(ipAddress)) {
            loadFactor = ipTable.get(ipAddress);
            loadFactor++;
        }
        ipTable.remove(ipAddress);
        ipTable.put(ipAddress, loadFactor);
    }

    public static String getIPAddress() {
        myArray = ipTable.entrySet().toArray();
        Arrays.sort(myArray, myComparator);
        String ipAddress = (String) ((Map.Entry) myArray[0]).getKey();
        System.out.println("IP address to return: " + ipAddress);
        return ipAddress;
    }

    private static void updateProcess(final String ipAddress) {
        runner = new Thread(new Runnable() {
            public void run() {
                if (ipTable.containsKey(ipAddress)) {
                    loadFactor = ipTable.get(ipAddress);
                    loadFactor++;
                }
                ipTable.remove(ipAddress);
                ipTable.put(ipAddress, loadFactor);

            }
        });
    }

    public static void main(String[] args) {
        insert("127.0.0.1");
        insert("126.0.0.1");
        update("127.0.0.1");
        insert("129.0.0.1");
        update("126.0.0.1");
        getIPAddress();
        System.out.println(ipTable);
    }
}

class IpComparator implements Comparator {
    public int compare(Object o1, Object o2) {
        if (((Integer) ((Map.Entry) o1).getValue()).intValue() > ((Integer) ((Map.Entry) o2).getValue()).intValue()) {
            return (1);
        } else if (((Integer) ((Map.Entry) o1).getValue()).intValue() < ((Integer) ((Map.Entry) o2).getValue()).intValue()) {
            return (-1);
        } else {
            return (0);
        }
    }
}