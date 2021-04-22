package xml_system.xml_access;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import xml_system.model.Item;
import xml_system.model.Section;
import xml_system.model.Warehouse;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WarehouseDOMParser {
    private DocumentBuilder documentBuilder;
    private final Warehouse warehouse;

    public WarehouseDOMParser() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            documentBuilder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        warehouse = new Warehouse();
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void parse(String path) {
        try {
            Document document = documentBuilder.parse(path);
            Element root = document.getDocumentElement();
            NodeList sectionsList = root.getElementsByTagName("section");
            List<Section> sections = new ArrayList<>();

            for (int i = 0; i < sectionsList.getLength(); i++) {
                Element sectionElement = (Element) sectionsList.item(i);
                Section section = buildSection(sectionElement);
                sections.add(section);
            }

            warehouse.setSections(sections);
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    private static Section buildSection(Element sectionElement) {
        Section section = new Section();

        section.setId(Integer.parseInt(sectionElement.getAttribute(WarehouseXMLComponents.SECTION_ID.toString())));
        section.setName(getTextContent(sectionElement, WarehouseXMLComponents.SECTION_NAME.toString()));

        Element itemsElement = (Element) sectionElement.getElementsByTagName(WarehouseXMLComponents.ITEMS.toString())
                                                       .item(0);
        NodeList itemsList = itemsElement.getElementsByTagName("item");
        List<Item> items = new ArrayList<>();

        for (int i = 0; i < itemsList.getLength(); i++) {
            Element itemElement = (Element) itemsList.item(i);
            Item item = buildItem(itemElement);
            items.add(item);
        }

        section.setItems(items);
        return section;
    }

    private static Item buildItem(Element itemElement) {
        Item item = new Item();

        item.setId(Integer.parseInt(itemElement.getAttribute(WarehouseXMLComponents.ITEM_ID.toString())));
        item.setName(getTextContent(itemElement, WarehouseXMLComponents.ITEM_NAME.toString()));
        item.setPrice(Integer.parseInt(getTextContent(itemElement, WarehouseXMLComponents.PRICE.toString())));

        return item;
    }

    private static String getTextContent(Element element, String elementName) {
        return element.getElementsByTagName(elementName).item(0).getTextContent();
    }
}