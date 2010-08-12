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
package org.epzilla.accumulator.service;


import java.io.*;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Hashtable;
import java.rmi.Remote;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;

import org.epzilla.accumulator.Constants;
import org.epzilla.accumulator.util.XMLElement;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Mar 2, 2010
 * Time: 7:43:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class ServiceLoader {
    //    Vector<XMLElement> services = new Vector<XMLElement>();
    Hashtable<String, Hashtable<String, String>> serviceMap = new Hashtable<String, Hashtable<String, String>>();

    public ServiceLoader() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("./src/services.xml"));
        String line = null;
        StringBuilder sb = new StringBuilder("");
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        XMLElement xe = new XMLElement();
        xe.parseString(sb.toString());

        for (XMLElement e : xe.getChildren()) {
//            services.add(e);
            Hashtable<String, String> table = new Hashtable<String, String>();
            for (XMLElement child : e.getChildren()) {
                table.put(child.getName().trim(), child.getContent().trim());
            }
            this.serviceMap.put(table.get(Constants.Name), table);
        }
    }

    /**
     * deploys services which are marked as 'autodeploy'.
     */
    public void autodeploy() throws ClassNotFoundException, IllegalAccessException, InstantiationException, MalformedURLException, RemoteException {

        for (Hashtable<String, String> ht : serviceMap.values()) {
            String deploy = ht.get(Constants.Autodeploy);
            if ("true".equals(deploy)) {
                Remote r = (Remote) Class.forName(ht.get(Constants.Class)).newInstance();
                Naming.rebind(ht.get("url"), r);
            }
        }

    }

    /**
     * loads the service with the given name.
     *
     * @param serviceName
     * @throws MalformedURLException
     * @throws RemoteException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public void loadService(String serviceName) throws MalformedURLException, RemoteException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Hashtable<String, String> ht = this.serviceMap.get(serviceName);
        Remote r = (Remote) Class.forName(ht.get(Constants.Class)).newInstance();
        Naming.rebind(ht.get("url"), r);

    }
}
