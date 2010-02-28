package org.epzilla.dispatcher;

import jstm.core.TransactedList;
import jstm.core.TransactedArray;

import java.util.Random;
import java.util.TimerTask;

import generatedObjectModels.triggerInfoObject;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: Feb 24, 2010
 * Time: 9:23:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClusterLeaderIpListManager {
    public static TransactedArray<String> ipList = new TransactedArray<String>(20);


    // Code For Testing Only -Dishan
    public static void loadSampleIPs() {
        final java.util.Timer timer1 = new java.util.Timer();
        timer1.schedule(new TimerTask() {
            int count = 0;
            @Override
            public void run() {
                if (ipList != null) {
                    String ip = "192.168.1." + String.valueOf(count);
                    ipList.set(count, ip);
                    count++;

                    if (count == 10)
                        timer1.cancel();
                }
            }
        }, 10, 500);
    }

}
