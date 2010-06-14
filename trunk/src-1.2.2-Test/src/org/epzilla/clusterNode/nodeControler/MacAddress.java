package org.epzilla.clusterNode.nodeControler;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

/**
 * Created by IntelliJ IDEA.
 * User: Chathura
 * Date: Jun 10, 2010
 * Time: 7:58:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class MacAddress {
    public String getMacAddress() {
        String macAddress = "";
        try {
            InetAddress address = InetAddress.getLocalHost();
            NetworkInterface ni = NetworkInterface.getByInetAddress(address);

            if (ni != null) {
                byte[] mac = ni.getHardwareAddress();
                macAddress = new String(mac);
                if (mac != null) {
                    for (int i = 0; i < mac.length; i++) {
                        System.out.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : "");
                    }
                }
            } else {
                System.out.println("Network Interface for the specified address is not found.");
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (java.net.UnknownHostException e) {
            e.printStackTrace();
        }
        return macAddress;
    }
}
