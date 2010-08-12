/***************************************************************************************************
Copyright 2009 - 2010 Harshana Eranga Martin, Dishan Metihakwala, Rajeev Sampath, Chathura Randika

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
****************************************************************************************************/
package org.epzilla.dispatcher.dataManager;

import org.epzilla.dispatcher.ui.*;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: Feb 19, 2010
 * Time: 9:02:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class NodeVariables {
    private static int port = 4444;
    private static String nodeIP = "localhost";
    private static String currentServerIP = "192.168.1.1";
    private static DispatcherUI mainUI;
    private static int dispatcherId = 0;

    public static int getDispatcherId() {
        return dispatcherId;
    }

    public static void setDispatcherId(int dispatcherId) {
        NodeVariables.dispatcherId = dispatcherId;
    }

    public static String getNodeIP() {
        return nodeIP;
    }

    public static void setNodeIP(String nodeIP) {
        NodeVariables.nodeIP = nodeIP;
    }

    public static String getCurrentServerIP() {
        return currentServerIP;
    }

    public static void setCurrentServerIP(String currentServerIP) {
        NodeVariables.currentServerIP = currentServerIP;
    }

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        NodeVariables.port = port;
    }


    public static DispatcherUI getMainUI() {
        return mainUI;
    }

    public static void setMainUI(DispatcherUI mainUI) {
        NodeVariables.mainUI = mainUI;
    }
}
