package org.epzilla.clusterNode.xml;

import org.epzilla.dispatcher.xml.XMLElement;
import org.epzilla.util.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * This class is to load the settings details relate to the cluster
 * Author: Chathura
 * Date: Apr 20, 2010
 * Time: 10:37:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClusterSettingsReader {
    public static ArrayList<String[]> getServerIPSettings(String filename) {
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
                items[0] = child.getAttribute("mesurmentInterval");
                items[1] = child.getAttribute("cpuUpperThreashHold");
                items[2] = child.getAttribute("memUpperThreashHold");
                items[3] = child.getAttribute("cpuLowerThreashHold");
                lis.add(items);
            }
        } catch (Exception e) {
            Logger.error("", e);
        }
        return lis;
    }

}