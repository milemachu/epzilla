package org.epzilla.nameserver.xmlLog;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Chathura
 * Date: May 13, 2010
 * Time: 11:24:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class XmlReader {
    public static Vector<String[]> readFile(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line = null;
        StringBuilder sb = new StringBuilder("");
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        XMLElement xe = new XMLElement();
        xe.parseString(sb.toString());

        Vector<String[]> lis = new Vector<String[]>();

        String[] items = new String[3];

        for (XMLElement child : xe.getChildren()) {
            items = new String[3];
            items[0] = child.getAttribute("Name");
            items[1] = child.getAttribute("IpAdrs");
            items[2] = child.getAttribute("Port");
            lis.add(items);
        }

        return lis;
    }
}
