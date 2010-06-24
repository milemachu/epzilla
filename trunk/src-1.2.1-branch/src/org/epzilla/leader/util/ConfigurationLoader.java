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
 * This class is a utility class where we use to load the necessary configuration data from the configuration files.
 * 
 * @author Administrator
 *
 */
public class ConfigurationLoader {

	public void loadConfig(){

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
		
		String clusterId=e.getAttribute("id");
		String componentType=e.getAttribute("component");
		
		Epzilla.setClusterID(Integer.parseInt(clusterId));
		if(componentType.equalsIgnoreCase(Component.NODE.name()))
			Epzilla.setComponentType(Component.NODE.name());
		else if(componentType.equalsIgnoreCase(Component.DISPATCHER.name()))
			Epzilla.setComponentType(Component.DISPATCHER.name());
		else
			Epzilla.setComponentType(Component.ACCUMULATOR.name());
		
		Hashtable<Integer, String> itemList=Epzilla.getComponentIpList();
		
		for (Element el : e.getChildren()) {			
			System.out.println(el.getContent() + " " + el.getAttribute("uid")+ " " + el.getAttribute("default"));
			itemList.put(el.getIntAttribute("uid"), el.getContent());
			if(el.getAttribute("default")!=null){
				Epzilla.setDefaultLeader(el.getContent());
//				Epzilla.setClusterLeader(el.getContent()); //Cannot tell like this
			}
			try {
				if(el.getContent().equalsIgnoreCase(InetAddress.getLocalHost().getHostAddress())){
					Epzilla.setUID(Long.parseLong(el.getAttribute("uid")));
				}
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void loadConstants(){
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
			String name=el.getName();
			if(name.equalsIgnoreCase("ComponentDiscovery"))
				SystemConstants.COMPONENT_DISCOVERY_TIME=Integer.parseInt(el.getContent());
			else if(name.equalsIgnoreCase("DiscoveryMulticast"))
				SystemConstants.DISCOVERY_MULTICAST_TIME=Integer.parseInt(el.getContent());
			else if(name.equalsIgnoreCase("LeaderRemoval"))
				SystemConstants.DEAD_LEADER_REMOVAL_DELAY=Integer.parseInt(el.getContent());
			else if(name.equalsIgnoreCase("UpdatePeriod"))
				SystemConstants.UPDATE_SERVICE_RUNNING_TIME=Integer.parseInt(el.getContent());
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
