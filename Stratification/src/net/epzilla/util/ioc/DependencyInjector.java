package net.epzilla.util.ioc;

import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Feb 25, 2010
 * Time: 1:47:40 PM
 * To change this template use File | Settings | File Templates.
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
