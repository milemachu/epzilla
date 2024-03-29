package org.epzilla.nameserver.loadbalance;

import org.epzilla.util.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Chathura
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

    public static String search(String clientID, int dirsize, String[] ipAddrs) {
        if (ipTable.containsKey(clientID)) {
            ipAddress = ipTable.get(clientID);
        } else if (!ipTable.containsKey(clientID)) {
            ipAddress = insertIntoTable(clientID, ipAddrs, dirsize);
        }
        if (isValidIp(ipAddress) == false) {
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
        } catch (IOException e) {
        }
        return status;
    }
}
