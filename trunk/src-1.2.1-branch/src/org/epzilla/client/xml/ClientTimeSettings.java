package org.epzilla.client.xml;

import org.epzilla.util.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: chathura
 * Date: Jun 18, 2010
 * Time: 5:10:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientTimeSettings {
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


            String[] items = new String[4];

            for (XMLElement child : xe.getChildren()) {
                items[0] = child.getAttribute("initIntervalEvent");
                items[1] = child.getAttribute("sendingIntervalEvent");
                items[2] = child.getAttribute("initIntervalTrigger");
                items[3] = child.getAttribute("sendingIntervalTrigger");
                lis.add(items);
            }
        } catch (Exception e) {
           Logger.error("File reader error:",e);
        }
        return lis;
    }
}
