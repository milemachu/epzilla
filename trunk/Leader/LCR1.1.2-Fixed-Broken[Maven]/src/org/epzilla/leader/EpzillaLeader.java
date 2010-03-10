package org.epzilla.leader;

import java.util.Vector;

import org.epzilla.leader.event.IEpzillaEventListner;

public class EpzillaLeader {
	private Vector<IEpzillaEventListner> clusterClientList;

	// Private constructor prevents instantiation from other classes
	private EpzillaLeader() {
		setClusterClientList(new Vector<IEpzillaEventListner>());
	}

	/**
	 * SingletonHolder is loaded on the first execution of
	 * Singleton.getInstance() or the first access to SingletonHolder.INSTANCE,
	 * not before.
	 */
	private static class EpzillaLeaderHolder {
		private static final EpzillaLeader INSTANCE = new EpzillaLeader();
	}

	public static EpzillaLeader getInstance() {
		return EpzillaLeaderHolder.INSTANCE;
	}

	/**
	 * When the Epzilla process leaves from the Leader, use this to null and
	 * remove all the listeners
	 * 
	 * @param clusterClientList
	 */
	public synchronized void setClusterClientList(
			Vector<IEpzillaEventListner> clusterClientList) {
		this.clusterClientList = clusterClientList;
	}

	public synchronized Vector<IEpzillaEventListner> getClusterClientList() {
		return clusterClientList;
	}

}
