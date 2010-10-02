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
package org.epzilla.client.xml;

import org.epzilla.util.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class ClientTimeSettings {
    /*
    method read configuration data from a XML file and
    returns the settings details as  a array list
    <client>
    <clientID initIntervalEvent="100" sendingIntervalEvent="100" initIntervalTrigger="500" sendingIntervalTrigger="30000" triggerSleepTime="10" initTriggerLoop="100" priorTriggerLoop="10"/>
    </client>
     */
    public static ArrayList<String[]> getClientTimeIntervals(String filename) {
        ArrayList<String[]> lis = new ArrayList<String[]>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filename));

            String line = null;
            StringBuilder sb = new StringBuilder("");
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            XMLElement xe = new XMLElement();
            xe.parseString(sb.toString());


            String[] items = new String[7];

            for (XMLElement child : xe.getChildren()) {
                items[0] = child.getAttribute("initIntervalEvent");
                items[1] = child.getAttribute("sendingIntervalEvent");
                items[2] = child.getAttribute("initIntervalTrigger");
                items[3] = child.getAttribute("sendingIntervalTrigger");
                items[4] = child.getAttribute("triggerSleepTime");
                items[5] = child.getAttribute("initTriggerLoop");
                items[6] = child.getAttribute("priorTriggerLoop");
                lis.add(items);
            }
        } catch (Exception e) {
            Logger.error("File reader error:", e);
        }
        return lis;
    }
}
