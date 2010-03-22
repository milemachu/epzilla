package net.epzilla.accumulator.service;

import net.epzilla.accumulator.util.XMLElement;
import net.epzilla.accumulator.Constants;

import java.io.*;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Hashtable;
import java.rmi.Remote;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;

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
