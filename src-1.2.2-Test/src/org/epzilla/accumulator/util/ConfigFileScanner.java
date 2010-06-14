package org.epzilla.accumulator.util;

import java.util.Hashtable;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Feb 28, 2010
 * Time: 8:44:30 AM
 * To change this template use File | Settings | File Templates.
 */
public class ConfigFileScanner {

    private Hashtable<String, String> map = new Hashtable<String, String>();

    public ConfigFileScanner(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line = null;
        String[] parts = null;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.length() < 1) {
                continue;
            }
             parts = line.split("=");
            map.put(parts[0].trim(), parts[1].trim());
        }
    }

    public String getParameter(String paramName) {
        return this.map.get(paramName);
    }

}
