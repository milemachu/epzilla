package org.epzilla.dispatcher.dataManager;

import java.util.ArrayList;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.epzilla.dispatcher.clusterHandler.*;

/**
 * Created by IntelliJ IDEA.
 * User: chathura
 * Date: May 2, 2010
 * Time: 4:24:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventManager {
    private static ArrayList<String> ipArr = new ArrayList<String>();
    private static ArrayList<String> idArr = new ArrayList<String>();
    private static boolean isLoaded = false;

    public static void sendEventsToClusters(ArrayList<String> eList,String clientID) {
        if (!isLoaded) {
            loadClusterDetails();
        }
        try {
            EventSender.acceptEventStream(ipArr, idArr, eList,clientID);
        } catch (MalformedURLException e) {
            System.err.println(e);
        } catch (NotBoundException e) {
           System.err.println(e);
        } catch (RemoteException e) {
           System.err.println(e);
        }
    }

    private static void loadClusterDetails() {
        ipArr = ClusterLeaderIpListManager.getClientIpList();
        idArr = ClusterLeaderIpListManager.getClientIdList();
        isLoaded = true;
    }
}
