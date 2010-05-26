package org.epzilla.leader;

import org.epzilla.leader.message.MessageMeta;
import org.epzilla.leader.util.Status;

/**
 * This class contains the LCR leader election algorithm implementation. All the
 * necessary LCR specific methods are here. Based on the assumption that 1 is
 * the highest priority in UID sequence.
 * 
 * @author Administrator
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
