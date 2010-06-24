package org.epzilla.common.discovery;

/**
 * This class contains the constant which are using in the dynamic service discovery component.
 * @author Administrator
 *
 */
public interface Constants {
	/*
	 * Multicast message delimiter uses in the component 
	 */
	public static String MULTICAST_DELIMITER="\u0001";
	
	/*
	 * TCP message delimiter uses in the component
	 */
	public static String TCP_UNICAST_DELIMITER="\u0002";
	
	/*
	 * Node leaders uses this delimiter when sending messages to dispatchers.
	 */
	public static String DISPATCHER_CLIENT_DELIMITER="\u0005";
	
	/*
	 * Node uses this delimiter when sending messages to node cluster leaders.
	 */
	public static String NODE_CLIENT_DELIMITER="\u0006";
	
	/*
	 * This is used to separate the cluster Id.
	 */
	public static String CLUSTER_ID_DELIMITER="\u0010";

}
