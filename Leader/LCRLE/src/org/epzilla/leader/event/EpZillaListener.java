package org.epzilla.leader.event;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class EpZillaListener implements IEpzillaEventListner {
	
	private String clientIp;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1964113377603043207L;

	public EpZillaListener() {
		try {
			clientIp= InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getData() {
		
		return clientIp;
	}
}
