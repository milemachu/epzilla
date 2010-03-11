package org.epzilla.leader.event;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class EpZillaListener implements IEpzillaEventListner {
/**
	 * 
	 */
	private static final long serialVersionUID = 545520804843206073L;
private String ipAddress;
	public EpZillaListener() {
		try {
			ipAddress= InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getData() {
		return ipAddress;
	}
}
