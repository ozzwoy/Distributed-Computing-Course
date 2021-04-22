package xml_system;

import xml_system.model.*;
import xml_system.xml_access.WarehouseDOMParser;
import xml_system.xml_access.WarehouseXMLCreator;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XMLTest {

    public static void main(String[] args) {
        String path = "src/main/resources/warehouse.xml";

        List<Section> sections = new ArrayList<>();
        List<Item> items = new ArrayList<>() {
            {
                add(new Item(23437, "Some1", 2));
                add(new Item(78433, "Some2", 3));
            }
        };
        sections.add(new Section(32433, "Section1", items));
        sections.add(new Section(42333, "Section2", items));
        Warehouse warehouse = new Warehouse(sections);

        WarehouseXMLCreator.createXML(warehouse, path);

        try {
            String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
            String schemaName = "src/main/resources/warehouse.xsd";
            SchemaFactory factory = SchemaFactory.newInstance(language);
            File schemaLocation = new File(schemaName);
            Schema schema = factory.newSchema(schemaLocation);
            Validator validator = schema.newValidator();
            Source source = new StreamSource(path);
            validator.validate(source);
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }

        WarehouseDOMParser parser = new WarehouseDOMParser();
        parser.parse(path);
        Warehouse warehouse1 = parser.getWarehouse();

        for (Section section : warehouse1.getSections()) {
            System.out.println(section.getId());
            System.out.println(section.getName());
            for (Item item : section.getItems()) {
                System.out.println(item.getId());
                System.out.println(item.getName());
                System.out.println(item.getPrice());
            }
        }
    }
}
