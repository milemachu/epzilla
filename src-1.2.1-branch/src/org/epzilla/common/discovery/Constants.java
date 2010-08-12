/***************************************************************************************************
Copyright 2009 - 2010 Harshana Eranga Martin, Dishan Metihakwala, Rajeev Sampath, Chathura Randika

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
****************************************************************************************************/
package org.epzilla.common.discovery;

/**
 * This class contains the constant which are using in the dynamic service discovery component.
 * @author Harshana Eranga Martin
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
