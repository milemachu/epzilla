package org.epzilla.dispatcher.dataManager;

import org.epzilla.dispatcher.clusterHandler.EventSender;
import org.epzilla.util.Logger;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.TimerTask;

/**
 * Created by IntelliJ IDEA.
 * This class is to manage the Events received to the Dispatcher
 * Author: Chathura
 * Date: May 2, 2010
 * Time: 4:24:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventManager {
    private static ArrayList<String> ipArr = new ArrayList<String>();
    private static ArrayList<String> idArr = new ArrayList<String>();
    private static boolean isLoaded = false;
    static java.util.Timer timer = new java.util.Timer();
    private static int UPDATE_SERVICE_RUNNING_TIME = 60000;
    private static int INITIAL_START_TIME = 3000;

    /*
    sending incoming event to the leaders of Node clusters
     */
    public static void sendEvents(byte[] event, String clientID) {
        if (!isLoaded) {
            loadClusterDetails();
        }
        try {
            for (int i = 0; i < ipArr.size(); i++) {
                EventSender.sendEvent(event, ipArr.get(i), idArr.get(i), clientID);
            }
            EventsCounter.setOutEventCount();

        } catch (MalformedURLException e) {
            Logger.error("Event send error:", e);
        } catch (NotBoundException e) {
            Logger.error("Event send error:", e);
        } catch (RemoteException e) {
            Logger.error("Event send error:", e);
        }

    }
   /*
   load details of node clusters
    */
    public static void loadClusterDetails() {

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                ipArr = ClusterLeaderIpListManager.getClusterIpList();
                idArr = ClusterLeaderIpListManager.getClusterIdList();
            }
        }, INITIAL_START_TIME, UPDATE_SERVICE_RUNNING_TIME);

        isLoaded = true;
    }


}
