package org.epzilla.client.controlers;

import org.epzilla.nameserver.NameService;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.UnknownHostException;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Chathura
 * Date: May 22, 2010
 * Time: 7:18:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class DynamicLookup {
    private static int timeOut = 3000;
    private static Vector<String> dispIP = new Vector<String>();


    public static void dynamicLookup() {
        try {
            NameService service = (NameService) ClientHandler.getNameSerObj();
            int size = service.getDirectorySize();
            for (int i = 0; i < size; i++) {
                dispIP.removeAllElements();
                String dispData = service.getDispatcherIP();
                StringTokenizer st = new StringTokenizer(dispData);
                String ip = st.nextToken();
                if (validate(ip)) {
                    dispIP.add(dispData);
                    ClientUIControler.setListLookup(dispIP);
                    ClientUIControler.setDispatcherData(dispData);
                    break;
                }

            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static boolean validate(String ip) {
        boolean status;
        try {
            status = InetAddress.getByName(ip).isReachable(timeOut);
            status = true;
        } catch (UnknownHostException e) {
            status = false;
        } catch (IOException e) {
            status = false;
        }
        return status;
    }
}
