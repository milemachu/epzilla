package org.epzilla.nameserver.xmlLog;

/***********************************************************************************
 * Copyright (c) 2010 Harshana Eranga Martin and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Harshana Eranga Martin <harshana05@gmail.com> - initial API and implementation
************************************************************************************/


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map.Entry;

public class XmlWriter {

	public static void writeToFile(String name,String ip, int port){

		try {
			BufferedWriter writer=new BufferedWriter(new FileWriter("./src/org/epzilla/nameserver/xmlLog/dispatcherData.xml"));

			writer.write("<Dispatcher>");
			writer.newLine();


				writer.write("\t"+"<Data Name=\""+name+"\" IpAdrs=\""+ip+"\" Port=\""+port+"\" />");
				writer.newLine();

			writer.write("</Dispatcher>");

			writer.flush();
			writer.close();
			writer=null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}

