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
package org.epzilla.leader;

import org.epzilla.leader.message.MessageMeta;
import org.epzilla.leader.util.Status;

/**
 * This class contains the LCR leader election algorithm implementation. All the
 * necessary LCR specific methods are here. Based on the assumption that 1 is
 * the highest priority in UID sequence.
 * 
 * @author Harshana Eranga Martin 	 mailto: harshana05@gmail.com
 * 
 */
public class LCRAlgoImpl {
	// This is the LCR logic
	private static String EMPTY_STRING=""; 
	public String runAlgorithm(String message) {
		String[] strArray = message.split(Character.toString(MessageMeta.SEPARATOR));
		if (strArray != null && Integer.parseInt(strArray[1]) >= 1) {
			int receivedUid = Integer.parseInt(strArray[1]);
			if (receivedUid == Epzilla.getUID()) {			
				return Status.LEADER.name();			
			} else if (receivedUid < Epzilla.getUID()) {				
				return Status.NON_LEADER.name();
			} else if (receivedUid > Epzilla.getUID()) {		
				return Status.UNKNOWN.name();
			}
		}	
		System.out.println("strArr 0:"+strArray[0]+ " Strarr1: "+strArray[1]);
		return EMPTY_STRING;	
	}
}
