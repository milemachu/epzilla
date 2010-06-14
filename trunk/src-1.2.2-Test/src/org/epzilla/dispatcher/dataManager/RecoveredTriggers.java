package org.epzilla.dispatcher.dataManager;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: Chathura
 * Date: May 28, 2010
 * Time: 9:24:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class RecoveredTriggers {
    public RecoveredTriggers() {

    }

    public static void getRecTriggerList(ArrayList<String> triggers) {

        String trigger,clientID,clusterID;
        try {
            for (String tr : triggers) {
                StringTokenizer st = new StringTokenizer(tr, ":");
                trigger = st.nextToken();  //trigger
                clientID = st.nextToken(); //client ID
                clusterID = st.nextToken();//cluster id
                sendTriggers(trigger, clientID, clusterID);
                System.out.println("Trigger: " + trigger + " Client: " + clientID + " Cluster: " + clusterID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendTriggers(String trigger, String clientID, String clusterID) {
        //trigger managing logic here

    }
}
