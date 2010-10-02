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
package org.epzilla.dispatcher.dataManager;

import org.epzilla.dispatcher.controlers.DispatcherUIController;
import org.epzilla.util.Logger;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * Take the recovered Triggers and add them to the STM of the Dispatcher
 * Author: Chathura
 * Date: May 28, 2010
 * Time: 9:24:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class RecoveredTriggers {
    private static ArrayList<String> tList = new ArrayList<String>();

    public RecoveredTriggers() {

    }
    /*
    * method accept arrayList of trigger information received from the Log based recovery
    * trigger list is split and get the target triggers and the relevant client id
     * recovered triggers are added to the TriggerManager class to distribute to the Clusters 
     */

    public static void getRecTriggerList(ArrayList<String> triggers) {
        tList.clear();
        String trigger = "", clientID = "", clusterID = "";
        try {
            for (String tr : triggers) {
                StringTokenizer st = new StringTokenizer(tr, ":");
                trigger = st.nextToken();  //trigger
                clientID = st.nextToken(); //client ID
                clusterID = st.nextToken();//cluster id
                tList.add(trigger);
                Logger.log("Trigger: " + trigger + " Client: " + clientID + " Cluster: " + clusterID);
            }
            TriggerManager.addAllTriggersToList(tList, clientID);
            DispatcherUIController.appendTriggers(tList);
        } catch (Exception e) {
            Logger.error("File read error; ", e);
        }
    }
}
