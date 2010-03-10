package org.epzilla.clusterNode.userInterface;

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
        instance.setVisible(true);

    }
}

