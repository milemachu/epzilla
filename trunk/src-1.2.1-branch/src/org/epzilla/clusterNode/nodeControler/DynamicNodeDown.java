package org.epzilla.clusterNode.nodeControler;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * This class is to shut down a Node dynamically
 * Author: Chathura
 * Date: Jun 10, 2010
 * Time: 7:42:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class DynamicNodeDown {

    public static void shutDown() throws IOException {
        Runtime runtime = Runtime.getRuntime();
        runtime.exec("shutdown -s -t 0");
        System.exit(0);
    }
}