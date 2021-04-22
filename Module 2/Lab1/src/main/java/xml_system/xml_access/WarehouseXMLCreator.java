package xml_system.xml_access;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import xml_system.model.Item;
import xml_system.model.Section;
import xml_system.model.Warehouse;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileWriter;
import java.io.IOException;

public class WarehouseXMLCreator {

    public static void createXML(Warehouse warehouse, String path) {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        Document document = null;

        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.newDocument();
            Element rootElement = document.createElementNS("http://www.example.com/warehouse",
                    "warehouse");
            rootElement.setPrefix("cns");
            document.appendChild(rootElement);

            for (Section section : warehouse.getSections()) {
                Element sectionElement = document.createElement("section");
                sectionElement.setAttribute("id", String.valueOf(section.getId()));

                Element sectionNameElement = document.createElement("name");
                sectionNameElement.appendChild(document.createTextNode(section.getName()));
                sectionElement.appendChild(sectionNameElement);

                Element itemsElement = document.createElement("items");

                for (Item item : section.getItems()) {
                    Element itemElement = document.createElement("item");
                    itemElement.setAttribute("id", String.valueOf(item.getId()));

                    Element itemNameElement = document.createElement("name");
                    itemNameElement.appendChild(document.createTextNode(item.getName()));
                    itemElement.appendChild(itemNameElement);

                    Element priceElement = document.createElement("price");
                    priceElement.appendChild(document.createTextNode(String.valueOf(item.getPrice())));
                    itemElement.appendChild(priceElement);

                    itemsElement.appendChild(itemElement);
                }

                if (section.getItems().isEmpty()) {
                    sectionElement.appendChild(document.createTextNode("<items></items>"));
                } else {
                    sectionElement.appendChild(itemsElement);
                }
                rootElement.appendChild(sectionElement);
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new FileWriter(path));
            transformer.transform(source, result);
        } catch (IOException | TransformerException e) {
            e.printStackTrace();
        }
    }
}
