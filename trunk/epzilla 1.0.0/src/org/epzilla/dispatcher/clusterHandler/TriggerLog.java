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
