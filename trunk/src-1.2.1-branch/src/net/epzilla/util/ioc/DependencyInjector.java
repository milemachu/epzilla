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
package net.epzilla.util.ioc;

import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Hashtable;

/**
 * used to inject dependencies based on a definition file.
 */
public class DependencyInjector {

    private Hashtable<String, String> classIdMap = new Hashtable<String, String>();

    
    public DependencyInjector(String fileName) throws IOException {
        this(new File(fileName));
    }

    /**
     * loads class id, fully qualified class name from the given file.
     * @param file   path of the file. the file should contain the format classId = fullyQualifiedName.
     * @throws IOException
     */
    public DependencyInjector(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = null;
        String[] parts = null;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.length() > 0) {
                parts = line.split("=");
                classIdMap.put(parts[0].trim(), parts[1].trim());
            }
        }
    }


    /**
     * creates an instance of the class which maps to the given class id.
     * @param classId
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public Object createInstance(String classId) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class c = Class.forName(classIdMap.get(classId));
        return c.newInstance();
    }

}
