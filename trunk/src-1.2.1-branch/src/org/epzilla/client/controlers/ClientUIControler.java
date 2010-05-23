package org.epzilla.client.controlers;

import org.epzilla.client.userInterface.ClientUI;

import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

public class ClientUIControler implements Runnable {
    private static ClientUI clientInstance;
    private ArrayList<String> notifications;
    private static String dateTime;
    private String message;

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

    public static void setListLookup(Vector<String> list) {
        clientInstance.isRegister = false;
        clientInstance.ips.removeAllElements();
        clientInstance.getListLookup().setListData(list);
    }

    public static void setDispatcherData(String str) {
//        clientInstance.setDispValues(str);
        initSend();
    }

    public static void initSend() {
        clientInstance.initProcess();
    }

    @Override
    public void run() {
        dateTime = getDateTime();
        clientInstance.getTxtResults().append(dateTime + ":" + message + "\n");

    }

    private static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
