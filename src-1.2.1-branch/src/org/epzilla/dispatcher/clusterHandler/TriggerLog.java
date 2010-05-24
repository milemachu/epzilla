package org.epzilla.dispatcher.clusterHandler;

import org.epzilla.dispatcher.logs.WriteLog;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: chathura
 * Date: Mar 25, 2010
 * Time: 2:24:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class TriggerLog {

    public static synchronized void writeTolog(String clusterID, ArrayList<String> triggers) {

        try {
            WriteLog.writeInit(triggers, clusterID);
        } catch (IOException e) {
        }

    }
}
