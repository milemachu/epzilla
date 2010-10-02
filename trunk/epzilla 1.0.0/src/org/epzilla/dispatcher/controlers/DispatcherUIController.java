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
package org.epzilla.dispatcher.controlers;

import org.epzilla.dispatcher.ui.DispatcherUI;
import org.epzilla.util.Logger;

import javax.swing.*;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: Feb 27, 2010
 * Time: 12:16:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class DispatcherUIController {
    private static DispatcherUI instance;
    private static String dateTime;

    public static void updateUI() {
        instance.repaint();
    }

    public static void InitializeUI() {
        instance = new DispatcherUI();
        instance.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        appendResults("Dispatcher Successfully deployed");
        try {
            Logger.log("entering... ");
            instance.register();
            Logger.log("returning...");
        } catch (MalformedURLException e) {
            appendResults("Setting details are incorrect");
        } catch (RemoteException e) {
            appendResults("Name Server not working...");
        } catch (UnknownHostException e) {
            appendResults("Setting details are incorrect");
        } catch (NotBoundException e) {
            appendResults("Setting details are incorrect");
        }
        DispatcherIPListManager.Initialize();
        loadDiscoveryStatus();
    }

    public static void setUIVisibility(boolean status) {
        instance.setVisible(status);
    }

    /**
     *  append Dispathcer status in the text area of the user interface
     * @param text
     */
    public static void appendTextToStatus(String text) {
        instance.getTxtStatus().append(text + "\n");
    }

    /**
     * append triggers to the Disapatcher interface
     * @param text
     */
    public static void appendTrigger(String text) {
        instance.getTxtTriggers().append(text + "\n");
    }

    /**
     * append recovered triggers to the user interface
     * @param triggers
     */

    public static void appendTriggers(List<String> triggers) {
        instance.getTxtRecoveredList().setText("");
        for (String trigger : triggers) {
            instance.getTxtRecoveredList().append(trigger + "\n");
        }
    }
    /*
   clear text area of triggers
    */

    public static void clearTriggerList() {
        instance.getTxtTriggers().setText("");
    }
    /*
    append the  IPs to the dispatcher user interface
     */

    public static void appendIP(String text) {
        instance.getTxtIPSet().append(text + "\n");
    }
    /*
     append the results to the dispatcher registration details on the settings panel
     */

    public static void appendResults(String text) {
        dateTime = getDateTime();
        instance.getTxtResult().append(dateTime + ":" + text + "\n");
    }
    /*
    set Dispathcer Dynamic discovery status
     */

    public static void dispDiscoveryStatus(String text) {
        instance.getTxtDiscoveryStatus().append(text + "\n");
    }
    /*
   clear the IP list
    */

    public static void clearIPList() {
        instance.getTxtIPSet().setText("");
    }
    /*
    set the incoming event count
     */

    public static void appendInEventsCount(String text) {
        instance.getTxtInEventCount().setText(text);
    }
    /*
   set out going event count
    */

    public static void appendOutEventCount(String text) {
        instance.getTxtOutEventCount().setText(text);
    }
    /*
    set event dispatching rate
     */

    public static void setEDRate(String text) {
        instance.getEventDispatchRate().setText(text);
    }
    /*
    append dispatcher ips in the dispatcher status tab
     */

    public static void appendDispatcherIPs(String text) {
        instance.getDispIPSet().append(text + "\n");
    }

    public static String getIpList() {
        return instance.getDispIPSet().getText();
    }

    public static void clearDispatcherIpList() {
        instance.getDispIPSet().setText("");
    }

    private static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private static void loadDiscoveryStatus() {
        dispDiscoveryStatus("Leader election service successfully deployed and running" + "\n" + "Dynamic Discovery process up and running....");
    }
}
