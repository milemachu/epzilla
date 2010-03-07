package org.epzilla.clusterNode;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: Mar 3, 2010
 * Time: 9:10:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class NodeController {
    private static int port = 4444;

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        NodeController.port = port;
    }
}
