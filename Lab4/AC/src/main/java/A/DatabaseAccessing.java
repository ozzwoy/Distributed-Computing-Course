package A;

import A.database.FullName;
import A.database.PhonebookDatabase;
import A.database.PhonebookEntry;
import A.rwlock.CustomReadWriteLock;
import A.threads.EntriesCorrectorThread;
import A.threads.FounderByNameThread;
import A.threads.FounderByNumberThread;

public class DatabaseAccessing {
    private static final String FILENAME = "src//main//java//A//phonebook.txt";
    private static final PhonebookEntry[] initEntries = new PhonebookEntry[] {
            new PhonebookEntry(new FullName("Lisnychenko", "Halyna", "Ivanivna"),
                    "+856473956473"),
            new PhonebookEntry(new FullName("Tereniv", "Mykola", "Panasovych"),
                    "+947584759324"),
            new PhonebookEntry(new FullName("Zilbermann", "Adam", "Isaakovych"),
                    "+564835574630"),
            new PhonebookEntry(new FullName("Levytska", "Oleksandra", "Yakivna"),
                    "+420402346335"),
            new PhonebookEntry(new FullName("Hirnychiy", "Taras", "Stepanovych"),
                    "+233475849302"),
            new PhonebookEntry(new FullName("Kaminska", "Svitlana", "Yuriyivna"),
                    "+423073085637")
    };
    private static final PhonebookEntry[] entriesToRemove = new PhonebookEntry[] {
            new PhonebookEntry(new FullName("Tereniv", "Mykola", "Panasovych"),
                    "+947584759324"),
            new PhonebookEntry(new FullName("Zilbermann", "Adam", "Isaakovych"),
                    "+233475849302"),
            new PhonebookEntry(new FullName("Kaminska", "Svitlana", "Yuriyivna"),
                    "+423073085637")
    };
    private static final FullName[] namesToFind = new FullName[] {
            new FullName("Lisnychenko", "Halyna", "Ivanivna"),
            new FullName("Tereniv", "Mykola", "Panasovych"),
            new FullName("Kaminska", "Svitlana", "Yuriyivna"),
            new FullName("Levytska", "Oleksandra", "Yakivna"),
            new FullName("Berezen", "Dmytro", "Vasyliovych"),
            new FullName("Komora", "Vasyl", "Serhiyovych")
    };
    private static final String[] numbersToFind = new String[] {
            "+564835574630",
            "+856473956473",
            "+748364862385",
            "+233475849302",
            "+128478274572"
    };

    public static void initDatabase(PhonebookDatabase database) {
        for (PhonebookEntry entry : initEntries) {
            database.append(entry);
        }
    }

    public static void main(String[] args) {
        PhonebookDatabase database = new PhonebookDatabase(FILENAME);
        initDatabase(database);
        CustomReadWriteLock lock = new CustomReadWriteLock();

        Thread corrector = new EntriesCorrectorThread(database, lock, entriesToRemove);
        Thread nameFounder = new FounderByNameThread(database, lock, namesToFind);
        Thread phoneFounder = new FounderByNumberThread(database, lock, numbersToFind);
        corrector.start();
        nameFounder.start();
        phoneFounder.start();
    }
}
