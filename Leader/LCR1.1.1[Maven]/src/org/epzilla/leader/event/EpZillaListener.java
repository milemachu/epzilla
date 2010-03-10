package org.epzilla.leader.event;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class EpZillaListener implements IEpzillaEventListner {

	public EpZillaListener() {

	}

	public String getData() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
