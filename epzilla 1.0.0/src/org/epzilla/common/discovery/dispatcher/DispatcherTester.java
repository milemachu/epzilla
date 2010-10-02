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
package org.epzilla.common.discovery.dispatcher;

import org.epzilla.common.discovery.multicast.MulticastSender;

public class DispatcherTester {
	private static String multicastGroupIp="224.0.0.2";
	private static int multicastPort=5005;
	private static String serviceName="DISPATCHER_SERVICE";	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MulticastSender broadcaster=new MulticastSender(multicastGroupIp,multicastPort);
		for (int i = 0; i < 5; i++) {
			broadcaster.broadcastMessage(serviceName);
		}
	}

}
