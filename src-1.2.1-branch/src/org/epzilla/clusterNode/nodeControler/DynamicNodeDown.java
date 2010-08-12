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
package org.epzilla.clusterNode.nodeControler;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * This class is to shut down a Node dynamically
 * Author: Chathura
 * Date: Jun 10, 2010
 * Time: 7:42:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class DynamicNodeDown {

    public static void shutDown() throws IOException {
        Runtime runtime = Runtime.getRuntime();
        runtime.exec("shutdown -s -t 0");
        System.exit(0);
    }
}
