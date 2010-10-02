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
package org.epzilla.leader.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Hashtable;

import org.epzilla.leader.Epzilla;
import org.epzilla.leader.xml.Element;

/**
 * This class is a utility class where we use to load the necessary
 * configuration data from the configuration files.
 * 
 * @author Harshana Eranga Martin mailto: harshana05@gmail.com
 * 
 */
public class ConfigurationLoader {

	/**
	 * Load configuration data from configuration files
	 */
	public void loadConfig() {

		StringBuilder sb = new StringBuilder("");
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("EpzillaIpConfig.xml"));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Element e = new Element();
		e.parseString(sb.toString()); // now the root is 'e'.
		System.out.println(e.getName());

		String clusterId = e.getAttribute("id");
		String componentType = e.getAttribute("component");

		Epzilla.setClusterID(Integer.parseInt(clusterId));
		if (componentType.equalsIgnoreCase(Component.NODE.name()))
			Epzilla.setComponentType(Component.NODE.name());
		else if (componentType.equalsIgnoreCase(Component.DISPATCHER.name()))
			Epzilla.setComponentType(Component.DISPATCHER.name());
		else
			Epzilla.setComponentType(Component.ACCUMULATOR.name());

		Hashtable<Integer, String> itemList = Epzilla.getComponentIpList();

		for (Element el : e.getChildren()) {
			System.out.println(el.getContent() + " " + el.getAttribute("uid")
					+ " " + el.getAttribute("default"));
			itemList.put(el.getIntAttribute("uid"), el.getContent());
			if (el.getAttribute("default") != null) {
				Epzilla.setDefaultLeader(el.getContent());
				// Epzilla.setClusterLeader(el.getContent()); //Cannot tell like
				// this
			}
			try {
				if (el.getContent().equalsIgnoreCase(
						InetAddress.getLocalHost().getHostAddress())) {
					Epzilla.setUID(Long.parseLong(el.getAttribute("uid")));
				}
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Load System constant values set by the user
	 */
	public void loadConstants() {
		StringBuilder sb = new StringBuilder("");
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("Constants.xml"));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Element e = new Element();
		e.parseString(sb.toString()); // now the root is 'e'.
		System.out.println(e.getName());

		for (Element el : e.getChildren()) {
			System.out.println(el.getName() + " " + el.getContent());
			String name = el.getName();
			if (name.equalsIgnoreCase("ComponentDiscovery"))
				SystemConstants.COMPONENT_DISCOVERY_TIME = Integer.parseInt(el
						.getContent());
			else if (name.equalsIgnoreCase("DiscoveryMulticast"))
				SystemConstants.DISCOVERY_MULTICAST_TIME = Integer.parseInt(el
						.getContent());
			else if (name.equalsIgnoreCase("LeaderRemoval"))
				SystemConstants.DEAD_LEADER_REMOVAL_DELAY = Integer.parseInt(el
						.getContent());
			else if (name.equalsIgnoreCase("UpdatePeriod"))
				SystemConstants.UPDATE_SERVICE_RUNNING_TIME = Integer
						.parseInt(el.getContent());
		}

		System.out.print("Done loading Constants.");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		System.out.print(".");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		System.out.println(".");
	}

	public static void main(String[] args) {
		new ConfigurationLoader().loadConfig();
		new ConfigurationLoader().loadConstants();
	}

}
