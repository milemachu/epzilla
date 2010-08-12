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

public class XmlWriter {

    public static void writeToFile(String name, String ip, int port) {

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("./src/org/epzilla/nameserver/xmlLog/dispatcherData.xml"));

            writer.write("<Dispatcher>");
            writer.newLine();


            writer.write("\t" + "<Data Name=\"" + name + "\" IpAdrs=\"" + ip + "\" Port=\"" + port + "\" />");
            writer.newLine();

            writer.write("</Dispatcher>");

            writer.flush();
            writer.close();
            writer = null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

}

