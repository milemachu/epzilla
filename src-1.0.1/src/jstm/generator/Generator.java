/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.generator;

import java.io.File;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import jstm.generator.cs.CSharpObjectModelWriter;
import jstm.generator.cs.CSharpStructureWriter;
import jstm.generator.files.FileGenerator;
import jstm.generator.java.JavaObjectModelWriter;
import jstm.generator.java.JavaStructureWriter;
import jstm.misc.Utils;

public class Generator {

    public enum Target {
        Java14, Java5, CSharp
    }

    private final ObjectModelDefinition _definition;

    private String _uid;

    private Target _target = Target.Java5;

    public Generator(ObjectModelDefinition definition) {
        if (definition.RootPackage == null)
            throw new IllegalArgumentException("Object model definition has no root package");

        _definition = definition;
    }

    public String getUID() {
        return _uid;
    }

    public void setUID(String value) {
        _uid = value;
    }

    public Target getTarget() {
        return _target;
    }

    public ObjectModelDefinition getObjectModelDefinition() {
        return _definition;
    }

    public static JAXBContext getJAXBContext() {
        ArrayList<Class> classes = new ArrayList<Class>();

        classes.add(ObjectModelDefinition.class);
        classes.add(Package.class);
        classes.add(Structure.class);
        classes.add(Field.class);
        classes.add(Method.class);
        classes.add(Argument.class);
        classes.add(ReturnValue.class);

        try {
            return JAXBContext.newInstance(classes.toArray(new Class[classes.size()]));
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public String getXML() {
        return _definition.toXML();
    }

    public void writeFiles(String folder, Target target) {
        writeFiles(folder, target, true);
    }

    public void writeFiles(String folder, Target target, boolean clearFolder) {
        if (_uid == null)
            _uid = Utils.createUID();

        _target = target;

        _definition.prepare();
        writePackages(folder, target, clearFolder);

        FileGenerator factory = createObjectModelWriter(_definition, folder);
        factory.write();
    }

    private void writePackages(String folder, Target target, boolean clearFolder) {
        ArrayList<String> dirs = new ArrayList<String>();

        for (Package p : _definition.getAllPackages()) {
            for (Structure structure : p.Structures) {
                String path = folder + "/" + p.getFullName().replace('.', '/');

                if (!dirs.contains(path)) {
                    File directory = new File(path);
                    directory.mkdirs();

                    if (clearFolder)
                        deleteDirectory(directory);

                    dirs.add(path);
                }

                createWriter(folder, structure).write();
            }
        }
    }

    protected FileGenerator createWriter(String folder, Structure structure) {
        if (getTarget() == Target.CSharp)
            return new CSharpStructureWriter(this, structure, folder);

        return new JavaStructureWriter(this, structure, folder);
    }

    protected FileGenerator createObjectModelWriter(ObjectModelDefinition builder, String folder) {
        if (getTarget() == Target.CSharp)
            return new CSharpObjectModelWriter(this, builder, folder);

        return new JavaObjectModelWriter(this, builder, folder);
    }

    protected static void deleteDirectory(File path) {
        if (path.exists()) {
            for (File file : path.listFiles()) {
                if (file.isDirectory())
                    deleteDirectory(file);
                else
                    file.delete();
            }
        }
    }
}
