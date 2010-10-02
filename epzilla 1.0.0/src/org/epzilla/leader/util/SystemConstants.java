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
package org.epzilla.leader.util;

/**
 * This class defines the constants we use in the Leader Election component.
 * 
 * @author Harshana Eranga Martin mailto: harshana05@gmail.com
 * 
 */
public class SystemConstants {
	/**
	 * Time to discover Components
	 */
	public static int COMPONENT_DISCOVERY_TIME = 0;
	/**
	 * Multicast messaging time span
	 */
	public static int DISCOVERY_MULTICAST_TIME = 0;
	/**
	 * Time till the system should start leader election upon detection of
	 * leader unavailable
	 */
	public static int DEAD_LEADER_REMOVAL_DELAY = 0;
	/**
	 * Time span to run update service
	 */
	public static int UPDATE_SERVICE_RUNNING_TIME = 0;

}
