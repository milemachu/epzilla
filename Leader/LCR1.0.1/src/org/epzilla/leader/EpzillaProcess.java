package org.epzilla.leader;

public class EpzillaProcess {

	// This is the UID of this host
	public static byte UID = 1;

	private String status = "unknown";

	// Private constructor prevents instantiation from other classes
	private EpzillaProcess() {
	}

	/**
	 * SingletonHolder is loaded on the first execution of
	 * Singleton.getInstance() or the first access to SingletonHolder.INSTANCE,
	 * not before.
	 */
	private static class EpzillaProcessHolder {
		private static final EpzillaProcess INSTANCE = new EpzillaProcess();
	}

	public static EpzillaProcess getInstance() {
		return EpzillaProcessHolder.INSTANCE;
	}

	public synchronized String getStatus() {
		return status;
	}

	public synchronized void setStatus(String status) {

		this.status = status;
	}

}
