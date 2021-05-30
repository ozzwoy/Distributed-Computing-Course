package cyb.dc.lab3.client.ui;

import cyb.dc.lab3.Commands;
import cyb.dc.lab3.client.Client;
import cyb.dc.lab3.dto.ItemDTO;
import cyb.dc.lab3.dto.SectionDTO;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class ClientUI {
    private JPanel mainPanel;
    private JPanel resultPanel;
    private JScrollPane resultScrollPane;
    private JLabel resultLabel;
    private JPanel operationsPanel;
    private JPanel commandPanel;
    private JComboBox<Commands> commandBox;
    private JPanel inputPanel;
    private JPanel field1;
    private JPanel field2;
    private JPanel field3;
    private JPanel field4;
    private JLabel field1Label;
    private JTextField textField1;
    private JLabel field2Label;
    private JLabel field3Label;
    private JLabel field4Label;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JButton executeButton;
    private JPanel field1Wrapper;
    private JPanel field2Wrapper;
    private JPanel field3Wrapper;
    private JPanel field4Wrapper;

    private Client client;
    private static final String ERROR_MESSAGE = "Some error occurred. Check input data correctness.";

    public ClientUI() {
        try {
            client = new Client("localhost", 8071);
        } catch (IOException e) {
            e.printStackTrace();
        }

        hideInputPanels();

        commandBox.addActionListener(e -> {
            resultLabel.setText("");
            Commands command = (Commands) commandBox.getSelectedItem();
            if (command != null) {
                switch (command) {
                    case CREATE_SECTION -> {
                        field1.setVisible(true);
                        field1Label.setText("Enter section name:");
                        field2.setVisible(false);
                        field3.setVisible(false);
                        field4.setVisible(false);
                    }
                    case CREATE_ITEM -> {
                        field1.setVisible(true);
                        field1Label.setText("Enter section ID:");
                        field2.setVisible(true);
                        field2Label.setText("Enter item name:");
                        field3.setVisible(true);
                        field3Label.setText("Enter price:");
                        field4.setVisible(false);
                    }
                    case DELETE_SECTION, FIND_SECTION, FIND_ITEMS_BY_SECTION -> {
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
                        field1Label.setText("Enter item ID: ");
                        field2.setVisible(true);
                        field2Label.setText("Enter section ID:");
                        field3.setVisible(true);
                        field3Label.setText("Enter item name:");
                        field4.setVisible(true);
                        field4Label.setText("Enter price:");
                    }
                    case SHOW_ALL_SECTIONS, SHOW_ALL_ITEMS -> {
                        field1.setVisible(false);
                        field2.setVisible(false);
                        field3.setVisible(false);
                        field4.setVisible(false);
                    }
                }
            }
        });

        executeButton.addActionListener(e -> {
            Commands command = (Commands) commandBox.getSelectedItem();
            if (command != null) {
                switch (command) {
                    case CREATE_SECTION -> onCreateSection();
                    case CREATE_ITEM -> onCreateItem();
                    case DELETE_SECTION -> onDeleteSection();
                    case DELETE_ITEM -> onDeleteItem();
                    case UPDATE_SECTION -> onUpdateSection();
                    case UPDATE_ITEM -> onUpdateItem();
                    case FIND_SECTION -> onFindSection();
                    case FIND_ITEM -> onFindItem();
                    case FIND_ITEMS_BY_SECTION -> onFindItemsBySection();
                    case SHOW_ALL_SECTIONS -> onShowAllSections();
                    case SHOW_ALL_ITEMS -> onShowAllItems();
                }
            }
            clearFields();
        });
    }

    private void createUIComponents() {
        commandBox = new JComboBox<>(Commands.values());
        commandBox.setSelectedItem(Commands.SHOW_ALL_SECTIONS);
    }

    private void onCreateSection() {
        String name = textField1.getText();
        SectionDTO section = new SectionDTO(0, name);

        if (client.insertSection(section)) {
            resultLabel.setText("Done! Inserted with ID " + section.getId() + ".");
        } else {
            resultLabel.setText("Some error occurred. Check input data correctness.");
        }

        textField1.setText("");
    }

    private void onCreateItem() {
        long sectionId = Long.parseLong(textField1.getText());
        String name = textField2.getText();
        int price = Integer.parseInt(textField3.getText());
        ItemDTO item = new ItemDTO(0, sectionId, name, price);

        if (client.insertItem(item)) {
            resultLabel.setText("Done! Inserted with ID " + item.getId() + ".");
        } else {
            resultLabel.setText(ERROR_MESSAGE);
        }

        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
    }

    private void onDeleteSection() {
        long id = Long.parseLong(textField1.getText());

        if (client.deleteSection(id)) {
            resultLabel.setText("Done!");
        } else {
            resultLabel.setText(ERROR_MESSAGE);
        }
    }

    private void onDeleteItem() {
        long id = Long.parseLong(textField1.getText());

        if (client.deleteItem(id)) {
            resultLabel.setText("Done!");
        } else {
            resultLabel.setText(ERROR_MESSAGE);
        }
    }

    private void onUpdateSection() {
        long id = Long.parseLong(textField1.getText());
        String name = textField2.getText();

        if (client.updateSection(new SectionDTO(id, name))) {
            resultLabel.setText("Done!");
        } else {
            resultLabel.setText(ERROR_MESSAGE);
        }
    }

    private void onUpdateItem() {
        long id = Long.parseLong(textField1.getText());
        long sectionId = Long.parseLong(textField2.getText());
        String name = textField3.getText();
        int price = Integer.parseInt(textField4.getText());

        if (client.updateItem(new ItemDTO(id, sectionId, name, price))) {
            resultLabel.setText("Done!");
        } else {
            resultLabel.setText(ERROR_MESSAGE);
        }
    }

    private void onFindSection() {
        long id = Long.parseLong(textField1.getText());
        SectionDTO section = client.findSection(id);

        if (section != null) {
            resultLabel.setText(toHTML(section.toString()));
        } else {
            resultLabel.setText(ERROR_MESSAGE);
        }
    }

    private void onFindItem() {
        long id = Long.parseLong(textField1.getText());
        ItemDTO item = client.findItem(id);

        if (item != null) {
            resultLabel.setText(toHTML(item.toString()));
        } else {
            resultLabel.setText(ERROR_MESSAGE);
        }
    }

    private void onFindItemsBySection() {
        long sectionId = Long.parseLong(textField1.getText());
        List<ItemDTO> items = client.findItemsBySection(sectionId);
        printItems(items);
    }

    private void onShowAllSections() {
        List<SectionDTO> sections = client.findAllSections();
        printSections(sections);
    }

    private void onShowAllItems() {
        List<ItemDTO> items = client.findAllItems();
        printItems(items);
    }

    private void printSections(List<SectionDTO> sections) {
        if (sections != null) {
            if (sections.isEmpty()) {
                resultLabel.setText("No sections found.");
            } else {
                StringBuilder builder = new StringBuilder();

                for (SectionDTO section : sections) {
                    builder.append(section.toString());
                }
                resultLabel.setText(toHTML(builder.toString()));
            }
        } else {
            resultLabel.setText(ERROR_MESSAGE);
        }
    }

    private void printItems(List<ItemDTO> items) {
        if (items != null) {
            if (items.isEmpty()) {
                resultLabel.setText("No items found.");
            } else {
                StringBuilder builder = new StringBuilder();

                for (ItemDTO item : items) {
                    builder.append(item.toString());
                }
                resultLabel.setText(toHTML(builder.toString()));
            }
        } else {
            resultLabel.setText(ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
    }

    private void hideInputPanels() {
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
        frame.setContentPane(new ClientUI().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        frame.setVisible(true);
    }
}
