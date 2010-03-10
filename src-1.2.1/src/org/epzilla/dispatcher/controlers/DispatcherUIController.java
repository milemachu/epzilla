package org.epzilla.dispatcher.controlers;

import javax.swing.*;

import org.epzilla.dispatcher.ui.*;

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
        instance.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        instance.setVisible(true);
    }

    public static void appendTextToStatus(String text) {
        instance.getTxtStatus().append(text + "\n");
    }

    public static void appendTrigger(String text) {
        instance.getTxtTriggers().append(text + "\n");
    }

    public static void appendIP(String text) {
        instance.getTxtIPSet().append(text + "\n");
    }
    public static void appendResults(String text){
    	instance.getTxtResult().append(text+ "\n");
    }
    public static void clearIPList() {
        instance.getTxtIPSet().setText("");
    }
}
