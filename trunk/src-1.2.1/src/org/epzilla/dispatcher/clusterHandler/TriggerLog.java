package org.epzilla.dispatcher.clusterHandler;

import org.epzilla.dispatcher.logs.WriteLog;

import java.util.TimerTask;
import java.util.Timer;
import java.util.Vector;
import java.util.ArrayList;
import java.awt.*;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: chathura
 * Date: Mar 25, 2010
 * Time: 2:24:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class TriggerLog {

    private static int delay = 0;
    private static int interval = 1000;
    private static Timer timer = new Timer();
    private static ArrayList<String> triggerlist = new ArrayList<String>();

     public static synchronized void writeTolog(String serverIp, final String clusterID, final byte[] stream){
         timer.scheduleAtFixedRate(new TimerTask()
        {
            public void run()
            {
                triggerlist.add(stream.toString()) ;
                try {
                    WriteLog.writeInit(triggerlist,clusterID);
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                timer.cancel();
            }
        }, delay, interval);
    }
}
