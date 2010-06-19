package org.epzilla.dispatcher.clusterHandler;

import org.epzilla.dispatcher.logs.WriteLog;
import org.epzilla.dispatcher.rmi.TriggerRepresentation;
import org.epzilla.util.Logger;

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

    public static synchronized void writeTolog(String clientID, String clusterID, ArrayList<TriggerRepresentation> triggers) {

        try {
            ArrayList<String> list = new ArrayList();
            for (TriggerRepresentation rep : triggers) {
                list.add(rep.getTrigger());
            }
            WriteLog.writeInit(list, clientID, clusterID);
        } catch (IOException e) {
            Logger.error("Logging Error: ",e);
        }

    }
}
