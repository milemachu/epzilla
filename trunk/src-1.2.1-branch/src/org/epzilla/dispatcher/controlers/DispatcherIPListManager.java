package org.epzilla.dispatcher.controlers;

import org.epzilla.leader.LeaderElectionInitiator;

import java.util.HashSet;
import java.util.Iterator;
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
                HashSet<String> ipList = LeaderElectionInitiator.getSubscribedNodeList();
                for (Iterator i = ipList.iterator(); i.hasNext();) {
                    String ip = (String) i.next();
                    DispatcherUIController.appendDispatcherIPs(ip);
                }
                System.gc();
            }
        }, 2000, 60000);
    }
}
