package org.epzilla.client.controlers;

import org.epzilla.client.ClientUI;

import javax.swing.*;

public class ClientUIControler implements Runnable {
    private static ClientUI clientInstance;
    private String message = "";

    public ClientUIControler() {
    }

    public ClientUIControler(String msg) {
        this.message = msg;
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
        clientInstance.getTxtResults().append(message + "\n");
    }

}
