package org.epzilla.clusterNode.xml;

import org.epzilla.dispatcher.xml.XMLElement;

import java.util.ArrayList;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Created by IntelliJ IDEA.
 * User: chathura
 * Date: Apr 20, 2010
 * Time: 10:37:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClusterSettingsReader {
    public ArrayList<String[]> getServerIPSettings(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line = null;
        StringBuilder sb = new StringBuilder("");
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        XMLElement xe = new XMLElement();
        xe.parseString(sb.toString());

        ArrayList<String[]> lis = new ArrayList<String[]>();

        String[] items = new String[3];

        for (XMLElement child : xe.getChildren()) {
            items = new String[1];
            items[0] = child.getAttribute("id");
            lis.add(items);
        }

        return lis;
    }

}