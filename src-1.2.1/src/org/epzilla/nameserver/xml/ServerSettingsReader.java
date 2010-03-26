package org.epzilla.nameserver.xml;

import org.epzilla.ui.xml.*;
import org.epzilla.ui.xml.XMLElement;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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

        String[] items = new String[1];

        for (XMLElement child : xe.getChildren()) {
            items = new String[1];
            items[0] = child.getAttribute("size");
            lis.add(items);
        }

        return lis;
    }

}