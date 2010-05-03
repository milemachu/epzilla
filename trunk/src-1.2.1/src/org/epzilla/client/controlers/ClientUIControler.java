package org.epzilla.client.controlers;

import org.epzilla.client.userInterface.ClientUI;

import javax.swing.*;
import java.util.ArrayList;

public class ClientUIControler implements Runnable {
    private static ClientUI clientInstance;
    private ArrayList<String> notifications;
    private String message = "";

    public ClientUIControler() {
    }

    public ClientUIControler(String msg) {
        this.message = msg;
    }

    public ClientUIControler(ArrayList<String> notifics) {
        this.notifications = notifics;
    }

    public static void initializeClientUI() {
        clientInstance = new ClientUI();
        clientInstance.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        clientInstance.setVisible(true);
    }

    public static void appendResults(String message) {
        clientInstance.getTxtResults().append(message + "\n");
    }

    @Override
    public void run() {
        for (String notification : notifications) {
            clientInstance.getTxtResults().append(notification + "\n");
        }
    }

}
