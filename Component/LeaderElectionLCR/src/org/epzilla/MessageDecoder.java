package org.epzilla;

import java.util.ArrayList;
import org.epzilla.util.Constant;

public class MessageDecoder {
//	private static String messageDelimiter="#";
	
	public static ArrayList<String> getMessageItems(String message){
		//Message Format IP#priority
		
		String ipAddress=null;
		String priority=null;
		
		ipAddress=message.substring(0,message.indexOf(Constant.LeaedrMessageParamDelimeter));
		priority=message.substring(message.indexOf(Constant.LeaedrMessageParamDelimeter)+1,message.indexOf(Constant.LeaderMessageDelimeter));
		
		ArrayList<String> items=new ArrayList<String>();
		items.add(ipAddress);
		items.add(priority);
		
		return items;
	}

}
