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
package org.epzilla.clusterNode.nodeControler;

import org.epzilla.util.Logger;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

/**
 * Created by IntelliJ IDEA.
 * This class is get the MAC address of ta computer
 * Author: Chathura
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
            Logger.error("socket eror:", e);
        } catch (java.net.UnknownHostException e) {
            Logger.error("host eror:", e);
        }
        return macAddress;
    }
}
