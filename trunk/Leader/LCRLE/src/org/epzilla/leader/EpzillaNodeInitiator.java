package org.epzilla.leader;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.apache.log4j.Logger;
import org.epzilla.leader.event.EpZillaListener;
import org.epzilla.leader.event.ProcessStatusChangedEvent;
import org.epzilla.leader.event.PulseReceivedEvent;
import org.epzilla.leader.rmi.LeaderMessageClient;
import org.epzilla.leader.rmi.LeaderRMIServer;
import org.epzilla.leader.util.ConfigurationLoader;
import org.epzilla.leader.util.Status;

public class EpzillaNodeInitiator {
	private static Logger logger = Logger.getLogger(EpzillaNodeInitiator.class);

	public EpzillaNodeInitiator() {

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

	public static void main(String[] args) throws UnknownHostException,
			RemoteException, MalformedURLException, NotBoundException {
		EpzillaNodeInitiator initiator = new EpzillaNodeInitiator();

		// Init the EpzillaProcess
		EpzillaProcess epzillaInstance = EpzillaProcess.getInstance();

		// LOAD CONFIGS via Configuration loader.
		try {
			new ConfigurationLoader().loadConfig(epzillaInstance);
			logger.info("Configuration loaded and ready to initialize");
		} catch (IOException e1) {
			logger
					.error("Configuration initialization failed. Exiting system.");
			e1.printStackTrace();
			System.exit(-1);
		}

		boolean isServicesDeployed = initiator.deployRmiInterace();

		if (isServicesDeployed) {
			logger
					.info("RMI Services deployied. Initializing start up sequence.");

			// Get this from JSTM
			boolean isSTMServerRunning = false;

			if (isSTMServerRunning) {
				// Get the server IP from the STM
				String serverIp = "192.168.1.63";
				// Nothing to worry. Just Join the cluster/STM
				logger
						.info("STM server is running and this node joinging the STM cluster.");
				// Change the status and other variables. Register with the
				// server.
				// epzillaInstance.setStatus(Status.NON_LEADER.toString());
				// epzillaInstance.setClusterLeader(InetAddress.getByName(serverIp));
				// epzillaInstance.setLeaderElectionRunning(false);
				// epzillaInstance.fireEpzillaEvent(new
				// ProcessStatusChangedEvent());
				setEpzillaStatusToNonLeader(epzillaInstance, InetAddress
						.getByName(serverIp));

				// Register with the server.
				// LeaderMessageClient.registerListenerWithLeader(serverIp, new
				// EpZillaListener());
				// epzillaInstance.fireEpzillaEvent(new
				// PulseReceivedEvent(serverIp));
				// Rewrite the configuration file to remove default leader.
			} else {
				// Before creating a server, need to find the leader here.

				boolean isDefaultLeader = epzillaInstance.isDefaultLeader();
				boolean isMessageDelivered = false;

				if (isDefaultLeader) {
					// This can initialize as the leader no matter what.
					// When UID received to the nodes, they step down to unknown
					// again. So no problem.
					logger
							.info("No STM is present in the cluster. Launching the leader election since this is the default server.");
					for (InetAddress nextIp : epzillaInstance
							.getClusterIpList()) {
						if (!nextIp.getHostAddress().equalsIgnoreCase(
								InetAddress.getLocalHost().getHostAddress())) {
							try {
								LeaderMessageClient.sendUidMessage(nextIp
										.getHostAddress());
								epzillaInstance.setLeaderElectionRunning(true);
								isMessageDelivered = true;
								break;
							} catch (Exception e) {
								continue;
							}
						} else {
							continue;
						}
					}

					// isMessageDelivered==false=> message was not delivered to
					// any one.
					// Wait any try again if it fails again, send UID to
					// localhost

					if (!isMessageDelivered) {
						logger.info("UID message not delivered. Trying again.");

						try {
							Thread.sleep(5000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						for (InetAddress nextIp : epzillaInstance
								.getClusterIpList()) {
							if (!nextIp.getHostAddress()
									.equalsIgnoreCase(
											InetAddress.getLocalHost()
													.getHostAddress())) {
								try {
									LeaderMessageClient.sendUidMessage(nextIp
											.getHostAddress());
									epzillaInstance
											.setLeaderElectionRunning(true);
									isMessageDelivered = true;
									break;
								} catch (Exception e) {
									continue;
								}
							} else {
								continue;
							}
						}

					}

					if (!isMessageDelivered) {
						// Still the nodes are not present
						// Send UID to local host=> will be the leader
						LeaderMessageClient.sendUidMessage(InetAddress
								.getLocalHost().getHostAddress());
						epzillaInstance.setLeaderElectionRunning(true);
						isMessageDelivered = true;
					}

				} else {
					// Now we have to find the default leader and do the work.
					// If the default is not here, do complex situation
					// handling.
					boolean leaderRunningInDefaultNode = false;
					String defaultNodeStatus = null;
					InetAddress defaultNode = epzillaInstance
							.getDefaultClusterLeader();

					try {

						leaderRunningInDefaultNode = LeaderMessageClient
								.isLeaderElectioRunningInRemote(defaultNode
										.getHostAddress());
						defaultNodeStatus = LeaderMessageClient
								.getStateFromRemote(defaultNode
										.getHostAddress());

					} catch (Exception e) {
						logger.info("Cannot connect to the default node.");
						System.out.println(e.getMessage());

						leaderRunningInDefaultNode = false;
						defaultNodeStatus = null;
					}

					if (defaultNodeStatus != null) {
						if (!leaderRunningInDefaultNode
								& defaultNodeStatus
										.equalsIgnoreCase(Status.UNKNOWN
												.toString())) {
							// Default just started
						} else if (leaderRunningInDefaultNode
								& defaultNodeStatus
										.equalsIgnoreCase(Status.UNKNOWN
												.toString())) {
							// Default just started the leader election
						} else if (!leaderRunningInDefaultNode
								& defaultNodeStatus
										.equalsIgnoreCase(Status.LEADER
												.toString())) {
							// leader elected
							// I missed the party. Do not bother. register a
							// listener with him.
							// Change the status and other variables.
							// epzillaInstance.setStatus(Status.NON_LEADER.toString());
							// epzillaInstance.setClusterLeader(epzillaInstance.getDefaultClusterLeader());
							// epzillaInstance.setLeaderElectionRunning(false);
							// epzillaInstance.fireEpzillaEvent(new
							// ProcessStatusChangedEvent());
							setEpzillaStatusToNonLeader(epzillaInstance,
									defaultNode);

							// Register with the server.
							// LeaderMessageClient.registerListenerWithLeader(epzillaInstance.getDefaultClusterLeader().getHostAddress(),
							// new EpZillaListener());
							// epzillaInstance.fireEpzillaEvent(new
							// PulseReceivedEvent(epzillaInstance.getDefaultClusterLeader().getHostAddress()));
						} else if (!leaderRunningInDefaultNode
								& defaultNodeStatus
										.equalsIgnoreCase(Status.NON_LEADER
												.toString())) {
							// get the leader from him and register with him
							String leaderFromRemote = LeaderMessageClient
									.getClusterLeaderFromRemote(defaultNode
											.getHostAddress());

							setEpzillaStatusToNonLeader(epzillaInstance,
									InetAddress.getByName(leaderFromRemote));
						}
					} else {
						// default is not present
						// Check with other's whether LE running

						for (InetAddress nextIp : epzillaInstance
								.getClusterIpList()) {
							if (!nextIp.getHostAddress()
									.equalsIgnoreCase(
											InetAddress.getLocalHost()
													.getHostAddress())) {

								String status = null;
								boolean isLERunning = false;

								try {
									status = LeaderMessageClient
											.getStateFromRemote(nextIp
													.getHostAddress());

									isLERunning = LeaderMessageClient
											.isLeaderElectioRunningInRemote(nextIp
													.getHostAddress());

								} catch (Exception e) {
									status = null;
									isLERunning = false;
								}

								if (status != null) {
									// Connection to this host is successful
									if (!isLERunning
											& status
													.equalsIgnoreCase(Status.UNKNOWN
															.toString())) {
										// Just started
										continue;
									} else if (isLERunning
											& status
													.equalsIgnoreCase(Status.UNKNOWN
															.toString())) {
										// LE running and UID received but not
										// the
										// LEADER. So cannot use
										continue;
									} else if (!isLERunning
											& status
													.equalsIgnoreCase(Status.LEADER
															.toString())) {
										// This is the server.
										setEpzillaStatusToNonLeader(
												epzillaInstance, nextIp);
										break;
									} else if (!isLERunning
											& status
													.equalsIgnoreCase(Status.NON_LEADER
															.toString())) {
										// This is a non leader
										String leaderFromRemote = LeaderMessageClient
												.getClusterLeaderFromRemote(nextIp
														.getHostAddress());

										setEpzillaStatusToNonLeader(
												epzillaInstance,
												InetAddress
														.getByName(leaderFromRemote));
										break;
									}
								} else {
									// Machine is not alive
									continue;
								}

							} else {
								continue;
								// This ip is the localhost
							}
						}

						if (epzillaInstance.getStatus().equalsIgnoreCase(
								Status.UNKNOWN.toString())
								& !epzillaInstance.isLeaderElectionRunning()) {
							// Host is still started up. Wait or start Leader

							try {
								Thread.sleep(10000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							for (InetAddress nextIp : epzillaInstance
									.getClusterIpList()) {
								if (!nextIp.getHostAddress().equalsIgnoreCase(
										InetAddress.getLocalHost()
												.getHostAddress())) {

									String status = null;
									boolean isLERunning = false;

									try {
										status = LeaderMessageClient
												.getStateFromRemote(nextIp
														.getHostAddress());

										isLERunning = LeaderMessageClient
												.isLeaderElectioRunningInRemote(nextIp
														.getHostAddress());

									} catch (Exception e) {
										status = null;
										isLERunning = false;
									}

									if (status != null) {
										// Connection to this host is successful
										if (!isLERunning
												& status
														.equalsIgnoreCase(Status.UNKNOWN
																.toString())) {
											// Just started
											continue;
										} else if (isLERunning
												& status
														.equalsIgnoreCase(Status.UNKNOWN
																.toString())) {
											// LE running and UID received but
											// not the
											// LEADER. So cannot use
											continue;
										} else if (!isLERunning
												& status
														.equalsIgnoreCase(Status.LEADER
																.toString())) {
											// This is the server.
											setEpzillaStatusToNonLeader(
													epzillaInstance, nextIp);
											break;
										} else if (!isLERunning
												& status
														.equalsIgnoreCase(Status.NON_LEADER
																.toString())) {
											// This is a non leader
											String leaderFromRemote = LeaderMessageClient
													.getClusterLeaderFromRemote(nextIp
															.getHostAddress());

											setEpzillaStatusToNonLeader(
													epzillaInstance,
													InetAddress
															.getByName(leaderFromRemote));
											break;
										}
									} else {
										// Machine is not alive
										continue;
									}

								} else {
									continue;
									// This ip is the localhost
								}
							}

						}

						if (epzillaInstance.getStatus().equalsIgnoreCase(
								Status.UNKNOWN.toString())
								& !epzillaInstance.isLeaderElectionRunning()) {
							// Still nothing. send the UID to localhost.
							LeaderMessageClient.sendUidMessage(InetAddress
									.getLocalHost().getHostAddress());
							epzillaInstance.setLeaderElectionRunning(true);

						}

					}

				}

			}

			// if(epzillaInstance.getStatus().equalsIgnoreCase(Status.UNKNOWN.toString())){
			// logger.error("This node cannot instantiate as a server or as a non leader.");
			// System.out.println("This node cannot instantiate as a server or as a non leader.");
			// System.exit(-1);
			// }

		} else {
			logger.info("RMI service deployment failed. System shutting down.");
			System.out
					.println("RMI service deployment failed. System shutting down.");
			System.exit(-1);
		}

	}

	private static void setEpzillaStatusToNonLeader(
			EpzillaProcess epzillaInstance, InetAddress clusterLeader)
			throws RemoteException, MalformedURLException, NotBoundException {

		epzillaInstance.setStatus(Status.NON_LEADER.toString());
		epzillaInstance.setClusterLeader(clusterLeader);
		epzillaInstance.setLeaderElectionRunning(false);
		epzillaInstance.fireEpzillaEvent(new ProcessStatusChangedEvent());

		// Register with the server.
		LeaderMessageClient.registerListenerWithLeader(clusterLeader
				.getHostAddress(), new EpZillaListener());
		epzillaInstance.fireEpzillaEvent(new PulseReceivedEvent(clusterLeader
				.getHostAddress()));
	}

}
