package org.epzilla.nameserver.loadbalance;

import java.util.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Chathura
 * Date: Mar 20, 2010
 * Time: 6:39:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoadBalancer {
    static int timeOut = 2000;
    static Hashtable<String,String> ipTable = new Hashtable<String,String>();
    static String ipAddress="";
    static Random generator = new Random();

    private static String insertIntoTable(String clientID, String[] ipAddrs,int dirSize){
        if(ipTable.isEmpty()){
           ipTable.put(clientID,ipAddrs[0]);
            return ipAddrs[0];
        }else{
           int id = selectRand(dirSize);
           ipTable.put(clientID,ipAddrs[id]);
            return ipAddrs[id];
        }
    }
    public static String search(String clientID, int dirsize, String[] ipAddrs){
        if(ipTable.containsKey(clientID)){
            ipAddress= (String)ipTable.get(clientID);
        }else if(!ipTable.containsKey(clientID)){
            ipAddress = insertIntoTable(clientID,ipAddrs,dirsize);
        }
        return ipAddress;
    }
    private static int selectRand(int dirSize){
       int id=1;
       id = generator.nextInt(dirSize-1);
        return id;
    }
    private boolean isValidIp(String ip){
    	boolean status = false;
    	try {
			status = InetAddress.getByName(ip).isReachable(timeOut);

    	} catch (UnknownHostException e) {
		} catch (IOException e) {
			}
		return status;
    }

}
