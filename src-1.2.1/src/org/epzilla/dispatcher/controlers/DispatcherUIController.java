package org.epzilla.dispatcher.controlers;

import org.epzilla.dispatcher.ui.DispatcherUI;

import javax.swing.*;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: Feb 27, 2010
 * Time: 12:16:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class DispatcherUIController {
    private static DispatcherUI instance;

    public static void InitializeUI() {
        instance = new DispatcherUI();
        instance.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        instance.setVisible(true);
        try {
            instance.register();
        } catch (MalformedURLException e) {
            appendResults("Settings details are incorrect");
        } catch (RemoteException e) {
            appendResults("Name Server not working...");
        } catch (UnknownHostException e) {
            appendResults("Settings details are incorrect");
        } catch (NotBoundException e) {
            appendResults("Settings details are incorrect");
        }
    }

    public static void appendTextToStatus(String text) {
        instance.getTxtStatus().append(text + "\n");
    }

    public static void appendTrigger(String text) {
        instance.getTxtTriggers().append(text + "\n");
    }

    public static void appendIP(String text) {
        instance.getClusterIPs().append(text + "\n"); //cluster IPs are displayed under the Cluster Details tab
        instance.getTxtIPSet().append(text + "\n");
    }

    public static void appendResults(String text) {
        instance.getTxtResult().append(text + "\n");
    }

    public static void clearIPList() {
        instance.getTxtIPSet().setText("");
    }

    public static void appendInEventsCount(String text) {
        instance.getTxtInEventCount().setText("");
        instance.getTxtInEventCount().setText(text);
    }

    public static void appendOutEventCount(String text) {

    }
}
