package org.epzilla.dispatcher.rmi;

import static org.epzilla.dispatcher.controlers.MainDispatcherController.run;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class DispatcherService {

    private static String serviceName = "DISPATCHER_SERVICE";

    public DispatcherService() {

    }

    public DispatcherService(String name) {
        this.serviceName = name;
    }

    private void bindDispatcher(String serviceName) throws RemoteException, UnknownHostException, MalformedURLException {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new OpenSecurityManager());
        }
        DispInterface dispInt = new DispImpl();
        InetAddress inetAddress;
        inetAddress = InetAddress.getLocalHost();
        String ipAddress = inetAddress.getHostAddress();
        String id = dispIdGen(ipAddress);
        String disServiceName = serviceName + id;
        String name = "rmi://" + ipAddress + "/" + disServiceName;
        Naming.rebind(name, dispInt);
        System.out.println("Dispatcher Service successfully deployed.....");

    }

    /*
      * generate dispatcher id
      */
    private String dispIdGen(String addr) {
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

    public static void main(String[] args) {

        try {
            DispatcherService service = new DispatcherService();
            service.bindDispatcher(serviceName);
            run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
