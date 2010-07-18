package org.epzilla.dispatcher;

import org.epzilla.dispatcher.controlers.DispatcherUIController;
import org.epzilla.nameserver.NameService;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * This class is to register the Dispatcher in the Name Server
 * Author: Chathura
 * Date: Jun 16, 2010
 * Time: 5:36:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class DispatcherRegister {

    public DispatcherRegister() {
    }
    /*
    * register Dispathcer in the Name Server
    */

    public static void register(String ip, String serviceName, String port, String dispatcherName) throws MalformedURLException, RemoteException, NotBoundException, UnknownHostException {
        String url = "rmi://" + ip + "/" + serviceName;
        NameService service = (NameService) Naming.lookup(url);
        InetAddress inetAddress = InetAddress.getLocalHost();
        String ipAddress = inetAddress.getHostAddress();
        int num = Integer.parseInt(port);
        String id = dispIdGen(ipAddress);
        String name = dispatcherName + id;
        int i = service.insertNode(name, ipAddress, num);
        if (i == 1)
            DispatcherUIController.appendResults("Dispatcher Successfully Registered in the Name Server");
        else
            DispatcherUIController.appendResults("Dispatcher Successfully Registered in the Name Server");

    }

    /*
      * generate dispatcher id
      */

    private static String dispIdGen(String addr) {
        String[] addrArray = addr.split("\\.");
        String temp = "";
        String value = "";
        for (int i = 0; i < addrArray.length; i++) {
            temp = addrArray[i].toString();
            while (temp.length() != 3) {
                temp = '0' + temp;
            }
            value += temp;
        }
        return value;
    }
}
