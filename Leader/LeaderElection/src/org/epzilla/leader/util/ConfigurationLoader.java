package org.epzilla.leader.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.epzilla.leader.Epzilla;
import org.epzilla.leader.xml.Element;

public class ConfigurationLoader {

	public void loadConfig(Epzilla epzillaInstance) throws IOException {

		StringBuilder sb = new StringBuilder("");
		BufferedReader br = new BufferedReader(new FileReader(
				"ClusterIPConfig.xml"));
		String line = null;
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		br.close();
		Element e = new Element();
		e.parseString(sb.toString()); // now the root is 'e'.
		System.out.println(e.getName());
		for (Element el : e.getChildren()) {
			System.out.println(el.getContent() + " " + el.getAttribute("uid")
					+ " " + el.getAttribute("default"));
//			epzillaInstance.getClsuterIpWithUid().put(
//					new Integer(el.getAttribute("uid")), el.getContent());
//			epzillaInstance.getClusterIpList().add(
//					InetAddress.getByName(el.getContent()));
//			if (el.getAttribute("default") != null) {
//				epzillaInstance.setDefaultClusterLeader(InetAddress
//						.getByName(el.getContent()));
//
//				if (el.getContent().equalsIgnoreCase(
//						InetAddress.getLocalHost().getHostAddress())) {
//					epzillaInstance.setDefaultLeader(true);
//				}
			}

//			if (el.getContent().equalsIgnoreCase(
//					InetAddress.getLocalHost().getHostAddress())) {
//				epzillaInstance.setUID(Byte.parseByte(el.getAttribute("uid")));
//			}

		}

	

	public static void main(String[] args) throws IOException {
		 StringBuilder sb = new StringBuilder("");
		 BufferedReader br = new BufferedReader(new
		 FileReader("ClusterIPConfig.textile"));
		 String line = null;
		 while ((line = br.readLine()) != null) {
		 sb.append(line);
		 }
		 br.close();
		 Element e = new Element();
		 e.parseString(sb.toString()); // now the root is 'e'.
		 System.out.println(e.getName());
		 for (Element el: e.getChildren()) {
		 System.out.println(el.getContent()+" "+el.getAttribute("UID")+" "+el.getAttribute("Default"));
			            
		 }
//		new ConfigurationLoader().loadConfig(EpzillaProcess.getInstance());
//		System.out.println();

	}

}
