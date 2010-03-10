package org.epzilla.leader;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.epzilla.leader.rmi.LeaderRMIServer;
import org.epzilla.leader.util.ConfigurationLoader;

public class EpzillaAltNodeInitiator {

	private static Logger logger = Logger
			.getLogger(EpzillaAltNodeInitiator.class);
	private static EpzillaProcess epzillaInstance;

	public EpzillaAltNodeInitiator() {
		epzillaInstance = EpzillaProcess.getInstance();

	}

	/**
	 * Use this method to deploy all the RMI services.
	 * 
	 * @return
	 */
	private boolean deployRmiInterace() {
		try {
			logger.info("Deploying RMI Services in:"
					+ InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return LeaderRMIServer.deployLeaderService();
	}

	public static void main(String[] args) {
		EpzillaAltNodeInitiator initiator = new EpzillaAltNodeInitiator();

		// LOAD CONFIGS via Configuration loader.
		try {
			new ConfigurationLoader().loadConfig(EpzillaProcess.getInstance());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		boolean isServicesDeployed = initiator.deployRmiInterace();

		if (isServicesDeployed) {
			logger
					.info("RMI Services deployied. Initializing start up sequence.");

			boolean isDefaultLeader = epzillaInstance.isDefaultLeader();

			if (isDefaultLeader) {
				// This might be a start up of the DL or returning of the DL
				// Have to poll other machines and fins out whether they are
				// running a LE or have a Leader or they also
				// just started. Use isLeaderRunning and Status to identify the
				// situation.

			}

		} else {
			logger.info("RMI service deployment failed. System shutting down.");
			System.exit(-1);
		}
	}
}