/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.generator;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "ObjectModelDefinition")
public class ObjectModelDefinition {

    public String Name;

    public Package RootPackage;

    private ArrayList<Package> _allPackages;

    private ArrayList<Structure> _allStructures;

    public ObjectModelDefinition() {
    }

    public ObjectModelDefinition(String name) {
        Name = name;
    }

    @XmlTransient
    public ArrayList<Package> getAllPackages() {
        return _allPackages;
    }

    @XmlTransient
    public ArrayList<Structure> getAllStructures() {
        return _allStructures;
    }

    protected void prepare() {
        _allPackages = new ArrayList<Package>();
        _allStructures = new ArrayList<Structure>();

        gather(RootPackage);

        for (Structure structure : _allStructures) {
            for (Field field : structure.Fields)
                field.Type.prepare();

            for (Method method : structure.Methods) {
                for (Argument argument : method.Arguments)
                    argument.Type.prepare();

                method.ReturnValue.Type.prepare();
            }
        }
    }

    private void gather(Package p) {
        if (_allPackages.contains(p))
            throw new IllegalStateException("Package " + p + " is present in two packages.");

        _allPackages.add(p);

        for (Package child : p.Packages) {
            child._package = p;
            gather(child);
        }

        for (Structure structure : p.Structures) {
            _allStructures.add(structure);

            if (structure._package != null && structure._package != p)
                throw new IllegalStateException("Structure " + structure + " is present in two packages.");

            structure._package = p;
        }
    }

    public String toXML() {
        JAXBContext context = Generator.getJAXBContext();

        try {
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty("jaxb.noNamespaceSchemaLocation", "http://www.xstm.net/schemas/xstm-0.3.xsd");
            StringWriter writer = new StringWriter();
            marshaller.marshal(this, writer);
            return writer.toString().replace("\"", "\\\"");
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public static ObjectModelDefinition fromXML(File file) {
        JAXBContext context = Generator.getJAXBContext();

        try {
            javax.xml.bind.Unmarshaller unmarshaller = context.createUnmarshaller();
            return (ObjectModelDefinition) unmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public static ObjectModelDefinition fromXML(String xml) {
        JAXBContext context = Generator.getJAXBContext();

        try {
            javax.xml.bind.Unmarshaller unmarshaller = context.createUnmarshaller();
            return (ObjectModelDefinition) unmarshaller.unmarshal(new StringReader(xml));
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
