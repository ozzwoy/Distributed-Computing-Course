package cyb.dc.lab3;

import cyb.dc.lab3.dto.ItemDTO;
import cyb.dc.lab3.dto.SectionDTO;

import java.util.*;

public class MessageManager {
    public static final String SPLIT_SYMBOL = "#";

    public static String sectionToString(SectionDTO section) {
        if (section != null) {
            return section.getId() + SPLIT_SYMBOL +
                    section.getName() + SPLIT_SYMBOL;
        } else {
            return "";
        }
    }

    public static String itemToString(ItemDTO item) {
        if (item != null) {
            return item.getId() + SPLIT_SYMBOL +
                    item.getSectionId() + SPLIT_SYMBOL +
                    item.getName() + SPLIT_SYMBOL +
                    item.getPrice() + SPLIT_SYMBOL;
        } else {
            return "";
        }
    }

    public static String sectionsListToString(List<SectionDTO> sections) {
        StringBuilder builder = new StringBuilder();

        for (SectionDTO section : sections) {
            builder.append(sectionToString(section)).append(SPLIT_SYMBOL);
        }

        return builder.toString();
    }

    public static String itemsListToString(List<ItemDTO> items) {
        StringBuilder builder = new StringBuilder();

        for (ItemDTO item : items) {
            builder.append(itemToString(item)).append(SPLIT_SYMBOL);
        }

        return builder.toString();
    }

    public static SectionDTO stringToSection(String str) {
        String[] parameters = str.split(SPLIT_SYMBOL);
        SectionDTO section = new SectionDTO();

        section.setId(Long.parseLong(parameters[0]));
        section.setName(parameters[1]);

        return section;
    }

    public static ItemDTO stringToItem(String str) {
        String[] parameters = str.split(SPLIT_SYMBOL);
        ItemDTO item = new ItemDTO();

        item.setId(Long.parseLong(parameters[0]));
        item.setSectionId(Long.parseLong(parameters[1]));
        item.setName(parameters[2]);
        item.setPrice(Integer.parseInt(parameters[3]));

        return item;
    }

    public static List<SectionDTO> stringToSectionsList(String str) {
        List<SectionDTO> result = new ArrayList<>();

        if (str.contains(SPLIT_SYMBOL + SPLIT_SYMBOL)) {
            String[] sectionsStrings = str.split(SPLIT_SYMBOL + SPLIT_SYMBOL);
            for (String sectionStr : sectionsStrings) {
                result.add(stringToSection(sectionStr));
            }
        }

        return result;
    }

    public static List<ItemDTO> stringToItemsList(String str) {
        List<ItemDTO> result = new ArrayList<>();

        if (str.contains(SPLIT_SYMBOL + SPLIT_SYMBOL)) {
            String[] itemsStrings = str.split(SPLIT_SYMBOL + SPLIT_SYMBOL);
            for (String itemStr : itemsStrings) {
                result.add(stringToItem(itemStr));
            }
        }

        return result;
    }
}
