package org.epzilla.clusterNode.userInterface;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: Mar 9, 2010
 * Time: 4:09:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class NodeUIController {
    private static NodeUI instance;

    public static NodeUI getInstance() {
        return instance;
    }

    public static void setInstance(NodeUI instance) {
        NodeUIController.instance = instance;
    }

    public static void InitializeUI() {
        instance = new NodeUI();
        instance.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        instance.setVisible(true);


    }

    public static void appendTextToStatus(String text) {
        instance.getJTextAreaStatus().append(text + "\n");
    }

    public static void appendTextToTriggerList(String text) {
        instance.getJTextAreaTriggers().append(text + "\n");
    }

    public static void appendTextToIPList(String text) {
        instance.getJTextAreaIPList().append(text + "\n");
    }

    public static void setLeaderStatus(String text)
    {
        instance.getJTextAreaLeader().setText(text);
    }

}



