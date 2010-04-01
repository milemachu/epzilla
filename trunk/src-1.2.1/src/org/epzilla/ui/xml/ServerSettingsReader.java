package org.epzilla.ui.xml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Mar 8, 2010
 * Time: 9:51:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class ServerSettingsReader {

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
            items = new String[3];
            items[0] = child.getAttribute("ip");
            items[1] = child.getAttribute("port");
            items[2] = child.getAttribute("name");
            lis.add(items);
        }

        return lis;
    }

}
