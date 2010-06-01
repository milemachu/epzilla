package org.epzilla.dispatcher.dataManager;

import org.epzilla.util.Logger;

import java.util.ArrayList;
import java.util.List;
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
    public void triggerList(ArrayList<String> recArray){
       printArray(recArray);
    }
    public static String sendTiggerList(ArrayList<String> triggers) {
        printArray(triggers);

        String CID = "";
//        String trigger="";
//        try {
//            for (String tr : triggers) {
//                StringTokenizer st = new StringTokenizer(tr, ":");
//                trigger = st.nextToken();  //trigger
//                CID = st.nextToken(); //Cluster ID
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return CID;
    }
    public static void printArray(ArrayList<String> array) {
        for (String anArray : array) {
            System.out.println(anArray);
        }
    }
}
