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

    public static void InitializeUI() {
        instance = new DispatcherUI();
        instance.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        instance.setVisible(true);
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
    }

    public static void appendTextToStatus(String text) {
        instance.getTxtStatus().append(text + "\n");
    }

    public static void appendTrigger(String text) {
        instance.getTxtTriggers().append(text + "\n");
    }

    public static void appendTriggers(List<String> triggers) {
        instance.getTxtRecoveredList().setText("");
        for (String trigger : triggers) {
            instance.getTxtRecoveredList().append(trigger + "\n");
        }
    }

    public static void appendIP(String text) {
        instance.getTxtIPSet().append(text + "\n");
    }

    public static void appendResults(String text) {
        dateTime = getDateTime();
        instance.getTxtResult().append(dateTime + ":" + text + "\n");
    }

    public static void dispDiscoveryStatus(String text) {
        dateTime = getDateTime();
        instance.getTxtDiscoveryStatus().append(dateTime + ":" + text + "\n");
    }

    public static void clearIPList() {
        instance.getTxtIPSet().setText("");
    }

    public static void appendInEventsCount(String text) {
        instance.getTxtInEventCount().setText(text);
    }

    public static void appendOutEventCount(String text) {
        instance.getTxtOutEventCount().setText(text);
    }

    public static void appendClusterData(String cluster, String cpuUsg, String mmUsg) {
        instance.getTxtClusterPerformance().setText("Cluster ID: " + cluster + "\n" + "CPU Usage: " + cpuUsg + "%" + "\n" + "Memory Usage: " + mmUsg + "MB" + "\n");
    }

    private static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

}
