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
