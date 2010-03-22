
package jstm.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

/**
 * You don't need this class for normal usage. It generates the schema to which
 * XML object model definitions must conform to.
 */
public class SchemaGenerator {
    private static final String SCHEMA_FILE = "xstm-0.3.xsd";

    public static void main(String[] args) throws Exception {
        final File baseDir = new File(".");

        class MySchemaOutputResolver extends SchemaOutputResolver {

            @Override
            public Result createOutput(String namespaceUri, String suggestedFileName) throws IOException {
                return new StreamResult(new File(baseDir, SCHEMA_FILE));
            }
        }

        JAXBContext context = Generator.getJAXBContext();
        context.generateSchema(new MySchemaOutputResolver());

        marshall(context);
        unmarshall(context);
    }

    public static void marshall(JAXBContext context) throws Exception {
        javax.xml.bind.Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty("jaxb.formatted.output", true);
        marshaller.setProperty("jaxb.noNamespaceSchemaLocation", SCHEMA_FILE);

        ObjectModelDefinition model = jstm.tests.generated.Main.getObjectModelDefinition();
        model.prepare();

        FileWriter writer = null;

        try {
            writer = new FileWriter("C:/test.xml");
            marshaller.marshal(model, writer);
        } finally {
            if (writer != null)
                writer.close();
        }
    }

    public static void unmarshall(JAXBContext context) throws Exception {
        javax.xml.bind.Unmarshaller unmarshaller = context.createUnmarshaller();
        ObjectModelDefinition model2 = (ObjectModelDefinition) unmarshaller.unmarshal(new File("C:/test.xml"));
        Generator generator = new Generator(model2);
        generator.writeFiles("./src", Generator.Target.Java5, false);
    }
}