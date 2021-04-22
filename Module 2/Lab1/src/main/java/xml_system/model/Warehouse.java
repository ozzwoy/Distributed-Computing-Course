package xml_system.model;

import java.util.ArrayList;
import java.util.List;

public class Warehouse {
    private List<Section> sections;

    public Warehouse() {
        sections = new ArrayList<>();
    }

    public Warehouse(List<Section> sections) {
        this.sections = sections;
    }

    public List<Section> getSections() {
        return sections;
    }

    public Section getSection(int id) {
        return sections.stream()
                .filter(section -> section.getId() == id)
                .findAny()
                .orElse(null);
    }

    public Item getItem(int id) {
        for (Section section : sections) {
            for (Item item : section.getItems()) {
                if (item.getId() == id) {
                    return item;
                }
            }
        }
        return null;
    }

    public Section getSectionByItem(int id) {
        for (Section section : sections) {
            if (section.getItem(id) != null) {
                return section;
            }
        }
        return null;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public boolean addSection(Section section) {
        if (getSection(section.getId()) != null) {
            return false;
        }
        sections.add(section);
        return true;
    }

    public boolean deleteSection(int id) {
        Section section = getSection(id);
        if (section == null) {
            return false;
        }
        sections.remove(section);
        return true;
    }

    public boolean updateSection(int id, String newName) {
        Section section = getSection(id);
        if (section == null) {
            return false;
        }
        section.setName(newName);
        return true;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for (Section section : sections) {
            result.append(section.toString());
        }

        return result.toString();
    }
}
