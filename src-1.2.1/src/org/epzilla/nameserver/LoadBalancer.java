package org.epzilla.nameserver;

import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
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
    int timeOut = 2000;
    Random generator = new Random();

    public int selectRandIP(ArrayList<String> array){
       int id;
       int arrLength = array.size();
       if(arrLength==1){
           id = 0;
           return id;
       }else{
       id = generator.nextInt(arrLength);
       if(isValidIp(array.get(id))==true){
        return id;
       }
       }
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
