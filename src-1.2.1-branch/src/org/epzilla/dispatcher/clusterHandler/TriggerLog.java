package org.epzilla.dispatcher.clusterHandler;

import org.epzilla.dispatcher.logs.WriteLog;
import org.epzilla.dispatcher.rmi.TriggerRepresentation;
import org.epzilla.util.Logger;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * This class is to take the trigger information and create the checkpoint in the Dispatcher
 * local hard disk
 * Author: Chathura
 * Date: Mar 25, 2010
 * Time: 2:24:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class TriggerLog {
    /**
     * add triggers to the log
     * @param clientID
     * @param clusterID
     * @param triggers
     */

    public static synchronized void writeTolog(String clientID, String clusterID, ArrayList<TriggerRepresentation> triggers) {

        try {
            ArrayList<String> list = new ArrayList();
            for (TriggerRepresentation rep : triggers) {
                list.add(rep.getTrigger());
            }
            WriteLog.writeInit(list, clientID, clusterID);
        } catch (IOException e) {
            Logger.error("Logging Error: ", e);
        }

    }
}
