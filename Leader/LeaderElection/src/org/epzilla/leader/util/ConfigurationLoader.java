package org.epzilla.leader.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

import org.epzilla.leader.Epzilla;
import org.epzilla.leader.xml.Element;

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
				Epzilla.setClusterLeader(el.getContent());
			}
		}
	}

	
public static void main(String[] args) {
	new ConfigurationLoader().loadConfig();
}
	

}