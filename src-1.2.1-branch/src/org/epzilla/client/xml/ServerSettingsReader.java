package org.epzilla.client.xml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/*
method read configuration data from a XML file and
returns the settings details as  a array list
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
