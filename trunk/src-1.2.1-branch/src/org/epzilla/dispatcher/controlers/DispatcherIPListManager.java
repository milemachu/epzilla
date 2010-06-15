package org.epzilla.dispatcher.controlers;

import org.epzilla.leader.LeaderElectionInitiator;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.TimerTask;

/**
 * Created by IntelliJ IDEA.
 * User: Chathura
 * Date: May 31, 2010
 * Time: 3:33:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class DispatcherIPListManager {
    public static void Initialize() {
        final java.util.Timer timer1 = new java.util.Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                DispatcherUIController.clearDispatcherIpList();
                HashSet<String> ipList = LeaderElectionInitiator.getSubscribedNodeList();
                if (ipList.size() == 0) {
                    try {
                        String currentList = DispatcherUIController.getIpList();
                        InetAddress inetAddress = InetAddress.getLocalHost();
                        String ipAddress = inetAddress.getHostAddress();
                        if (!currentList.contains(ipAddress))
                            DispatcherUIController.appendDispatcherIPs(ipAddress);
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                } else {
                    String currentList = DispatcherUIController.getIpList();
                    for (Object dispList : ipList) {
                        String ip = (String) dispList;
                        if (!currentList.contains(ip))
                            DispatcherUIController.appendDispatcherIPs(ip);
                    }
                    System.gc();
                }
            }
        }, 5000, 30000);
    }
}
