package org.epzilla.clusterNode.userInterface;

import org.epzilla.leader.LeaderElectionInitiator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.TimerTask;

/**
 * Created by IntelliJ IDEA.
 * User: Chathura
 * Date: May 31, 2010
 * Time: 2:14:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class IpListManager {

    public static void Initialize() {
        final java.util.Timer timer1 = new java.util.Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                HashSet<String> ipList = LeaderElectionInitiator.getSubscribedNodeList();
                for (Iterator i = ipList.iterator(); i.hasNext();) {
                    String ip = (String) i.next();
                    NodeUIController.appendTextToIPList(ip);
                }
                System.gc();
            }
        }, 2000, 60000);
    }

}
