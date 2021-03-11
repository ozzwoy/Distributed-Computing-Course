package A.database;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PhonebookDatabase {
    private File file;

    public PhonebookDatabase(String fileName) {
        this.file = new File(fileName);
        try {
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void append(PhonebookEntry record) {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {
            writer.println(record.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void remove(PhonebookEntry toRemove) {
        String pattern = toRemove.toString();
        String entryStr;
        File newFile = new File(file.getAbsoluteFile().getParent() + "\\temp.txt");

        try (PrintWriter newWriter = new PrintWriter(new BufferedWriter(new FileWriter(newFile, true)))) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                while ((entryStr = reader.readLine()) != null) {
                    if (!entryStr.equals(pattern)) {
                        newWriter.println(entryStr);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String path = file.getPath();
        if (file.delete()) {
            File temp = new File(path);
            if (newFile.renameTo(temp)) {
                file = temp;
            }
        }
    }

    public List<PhonebookEntry> findByName(FullName fullName) {
        List<PhonebookEntry> result = new ArrayList<>();
        String pattern = fullName.toString();
        String entryStr;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while ((entryStr = reader.readLine()) != null) {
                String[] entrySplit = entryStr.split(" - ");
                String entryName = entrySplit[0];
                String entryPhone = entrySplit[1];

                if (entryName.equals(pattern)) {
                    String[] entryNameSplit = entryName.split(" ");
                    result.add(new PhonebookEntry(new FullName(entryNameSplit[0], entryNameSplit[1], entryNameSplit[2]),
                                                  entryPhone));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public List<PhonebookEntry> findByNumber(String number) {
        List<PhonebookEntry> result = new ArrayList<>();
        String entryStr;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while ((entryStr = reader.readLine()) != null) {
                String[] entrySplit = entryStr.split(" - ");
                String entryName = entrySplit[0];
                String entryPhone = entrySplit[1];

                if (entryPhone.equals(number)) {
                    String[] entryNameSplit = entryName.split(" ");
                    result.add(new PhonebookEntry(new FullName(entryNameSplit[0], entryNameSplit[1], entryNameSplit[2]),
                            entryPhone));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
