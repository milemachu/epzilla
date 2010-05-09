package org.epzilla.leader.event.listner;

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
			e.printStackTrace();
		}
	}
	
	public EpZillaListener(String clientIp){
		this.clientIp=clientIp;
	}

	public String getData() {
		
		return clientIp;
	}
}
