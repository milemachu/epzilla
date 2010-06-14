package org.epzilla.dispatcher.xml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Chathura
 * Date: Jun 14, 2010
 * Time: 9:11:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class DiscoveryStatusReader {
    public static ArrayList<String[]> getDiscoveryStatus(String filename) throws IOException {
           BufferedReader br = new BufferedReader(new FileReader(filename));
           String line;
           StringBuilder sb = new StringBuilder("");
           while ((line = br.readLine()) != null) {
               sb.append(line);
           }
           XMLElement xe = new XMLElement();
           xe.parseString(sb.toString());

           ArrayList<String[]> lis = new ArrayList<String[]>();

           String[] items;

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
