package xml_system.ui;

import org.xml.sax.SAXException;
import xml_system.model.Item;
import xml_system.model.Section;
import xml_system.model.Warehouse;
import xml_system.xml_access.WarehouseDOMParser;
import xml_system.xml_access.WarehouseXMLCreator;

import javax.swing.*;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ClientForm {
    private JPanel mainPanel;
    private JComboBox<Operations> operationBox;
    private JPanel inputPanel;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JLabel operationLabel;
    private JLabel field1Label;
    private JLabel field2Label;
    private JLabel field3Label;
    private JLabel field4Label;
    private JButton executeButton;
    private JPanel field1;
    private JPanel field2;
    private JPanel field3;
    private JPanel field4;
    private JLabel resultLabel;
    private JPanel field1Wrapper;
    private JPanel field2Wrapper;
    private JPanel field3Wrapper;
    private JPanel field4Wrapper;

    private static final String xmlPath = "src/main/resources/warehouse.xml";
    private static final String xsdPath = "src/main/resources/warehouse.xsd";
    private static Warehouse warehouse = new Warehouse();

    public ClientForm() {
        if (new File(xmlPath).exists()) {
            WarehouseDOMParser parser = new WarehouseDOMParser();
            parser.parse(xmlPath);
            warehouse = parser.getWarehouse();

            try {
                String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
                SchemaFactory factory = SchemaFactory.newInstance(language);
                File schemaLocation = new File(xsdPath);
                Schema schema = factory.newSchema(schemaLocation);
                Validator validator = schema.newValidator();
                Source source = new StreamSource(xmlPath);
                validator.validate(source);
            } catch (SAXException e) {
                resultLabel.setText("File warehouse.xml has some problems with structure.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            resultLabel.setText("Validated successfully.");
        }

        hideFields();

        operationBox.addActionListener(e -> {
            resultLabel.setText("");
            Operations operation = (Operations) operationBox.getSelectedItem();
            if (operation != null) {
                switch (operation) {
                    case CREATE_SECTION -> {
                        field1.setVisible(true);
                        field1Label.setText("Enter section ID:");
                        field2.setVisible(true);
                        field2Label.setText("Enter section name:");
                        field3.setVisible(false);
                        field4.setVisible(false);
                    }
                    case CREATE_ITEM -> {
                        field1.setVisible(true);
                        field1Label.setText("Enter item ID:");
                        field2.setVisible(true);
                        field2Label.setText("Enter item name:");
                        field3.setVisible(true);
                        field3Label.setText("Enter price:");
                        field4.setVisible(true);
                        field4Label.setText("Enter section ID:");
                    }
                    case DELETE_SECTION, FIND_SECTION -> {
                        field1.setVisible(true);
                        field1Label.setText("Enter section ID:");
                        field2.setVisible(false);
                        field3.setVisible(false);
                        field4.setVisible(false);
                    }
                    case DELETE_ITEM, FIND_ITEM -> {
                        field1.setVisible(true);
                        field1Label.setText("Enter item ID:");
                        field2.setVisible(false);
                        field3.setVisible(false);
                        field4.setVisible(false);
                    }
                    case UPDATE_SECTION -> {
                        field1.setVisible(true);
                        field1Label.setText("Enter section ID:");
                        field2.setVisible(true);
                        field2Label.setText("Enter new section name:");
                        field3.setVisible(false);
                        field4.setVisible(false);
                    }
                    case UPDATE_ITEM -> {
                        field1.setVisible(true);
                        field1Label.setText("Enter item ID:");
                        field2.setVisible(true);
                        field2Label.setText("Enter new item name:");
                        field3.setVisible(true);
                        field3Label.setText("Enter new price:");
                        field4.setVisible(false);
                    }
                    case SHOW_ALL_SECTIONS -> {
                        field1.setVisible(false);
                        field2.setVisible(false);
                        field3.setVisible(false);
                        field4.setVisible(false);
                    }
                }
            }
        });

        executeButton.addActionListener(e -> {
            Operations operation = (Operations) operationBox.getSelectedItem();
            if (operation != null) {
                switch (operation) {
                    case CREATE_SECTION -> onCreateSection();
                    case CREATE_ITEM -> onCreateItem();
                    case DELETE_SECTION -> onDeleteSection();
                    case DELETE_ITEM -> onDeleteItem();
                    case UPDATE_SECTION -> onUpdateSection();
                    case UPDATE_ITEM -> onUpdateItem();
                    case FIND_SECTION -> onFindSection();
                    case FIND_ITEM -> onFindItem();
                    case SHOW_ALL_SECTIONS -> onShowAllSections();
                }
            }
            clearFields();
        });
    }

    private void createUIComponents() {
        operationBox = new JComboBox<>(Operations.values());
        operationBox.setSelectedItem(Operations.SHOW_ALL_SECTIONS);
    }

    private void onCreateSection() {
        int id = Integer.parseInt(textField1.getText());
        String name = textField2.getText();

        if (warehouse.addSection(new Section(id, name, new ArrayList<>()))) {
            resultLabel.setText("Done!");
        } else {
            resultLabel.setText("Error. Section with such ID already exists.");
        }

        textField1.setText("");
        textField2.setText("");
    }

    private void onCreateItem() {
        int id = Integer.parseInt(textField1.getText());
        String name = textField2.getText();
        int price = Integer.parseInt(textField3.getText());
        int sectionId = Integer.parseInt(textField4.getText());

        if (warehouse.getItem(id) != null) {
            resultLabel.setText("Error. Item with such ID already exists.");
            return;
        }

        Section section = warehouse.getSection(sectionId);
        if (section == null) {
            resultLabel.setText("Error. Section with such ID does not exist.");
        } else {
            Item item = new Item(id, name, price);
            if (section.addItem(item)) {
                resultLabel.setText("Done!");
            } else {
                resultLabel.setText("Error. Item with such ID already exists.");
            }
        }
    }

    private void onDeleteSection() {
        int id = Integer.parseInt(textField1.getText());

        if (warehouse.deleteSection(id)) {
            resultLabel.setText("Done!");
        } else {
            resultLabel.setText("Error. Section with such ID does not exist.");
        }
    }

    private void onDeleteItem() {
        int id = Integer.parseInt(textField1.getText());
        Section section = warehouse.getSectionByItem(id);

        if (section == null) {
            resultLabel.setText("Error. Item with such ID does not exist.");
        } else {
            section.deleteItem(id);
            resultLabel.setText("Done!");
        }
    }

    private void onUpdateSection() {
        int id = Integer.parseInt(textField1.getText());
        String name = textField2.getText();

        if (warehouse.updateSection(id, name)) {
            resultLabel.setText("Done!");
        } else {
            resultLabel.setText("Error. Section with such ID does not exist.");
        }
    }

    private void onUpdateItem() {
        int id = Integer.parseInt(textField1.getText());
        String name = textField2.getText();
        int price = Integer.parseInt(textField3.getText());
        Section section = warehouse.getSectionByItem(id);

        if (section == null) {
            resultLabel.setText("Error. Item with such ID does not exists.");
        } else {
            section.updateItem(id, name, price);
            resultLabel.setText("Done!");
        }
    }

    private void onFindSection() {
        int id = Integer.parseInt(textField1.getText());
        Section section = warehouse.getSection(id);

        if (section != null) {
            resultLabel.setText(toHTML(section.toString()));
        } else {
            resultLabel.setText("Error. Section with such ID does not exist.");
        }
    }

    private void onFindItem() {
        int id = Integer.parseInt(textField1.getText());
        Item item = warehouse.getItem(id);

        if (item == null) {
            resultLabel.setText("Error. Item with such ID does not exist.");
        } else {
            resultLabel.setText(toHTML(item.toString()));
        }
    }

    private void onShowAllSections() {
        resultLabel.setText(toHTML(warehouse.toString()));
    }

    private void clearFields() {
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
    }

    private void hideFields() {
        field1.setVisible(false);
        field2.setVisible(false);
        field3.setVisible(false);
        field4.setVisible(false);
    }

    private static String toHTML(String str) {
        return "<html>" + str.replace("\n", "<br>") + "</html>";
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setContentPane(new ClientForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                WarehouseXMLCreator.createXML(warehouse, xmlPath);
            }
        });
    }
}
