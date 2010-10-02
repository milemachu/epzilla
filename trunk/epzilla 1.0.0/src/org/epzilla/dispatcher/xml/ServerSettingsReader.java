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
package org.epzilla.dispatcher.xml;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Mar 8, 2010
 * Time: 9:51:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class ServerSettingsReader {

    public static ArrayList<String[]> getServerIPSettings(String filename) throws IOException {
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
