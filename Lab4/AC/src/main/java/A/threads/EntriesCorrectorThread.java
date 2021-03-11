package A.threads;

import A.database.FullName;
import A.database.PhonebookDatabase;
import A.database.PhonebookEntry;
import A.rwlock.CustomReadWriteLock;

import java.util.Random;

public class EntriesCorrectorThread extends Thread {
    private final PhonebookDatabase database;
    private final CustomReadWriteLock lock;
    private final PhonebookEntry[] entriesToRemove;
    private final PhonebookEntry[] entriesToAppend;

    public EntriesCorrectorThread(PhonebookDatabase database, CustomReadWriteLock lock,
                                  PhonebookEntry[] entriesToRemove) {
        this.database = database;
        this.lock = lock;
        this.entriesToRemove = entriesToRemove;

        this.entriesToAppend = new PhonebookEntry[] {
                new PhonebookEntry(new FullName("Berezen", "Dmytro", "Vasyliovych"),
                        "+647365278032"),
                new PhonebookEntry(new FullName("Komora", "Vasyl", "Serhiyovych"),
                        "+547257753793"),
                new PhonebookEntry(new FullName("Kononenko", "Maryna", "Borysivna"),
                        "+748364862385"),
                new PhonebookEntry(new FullName("Kostenko", "Lina", "Stepanivna"),
                        "+128478274572")
        };
    }

    @Override
    public void run() {
        Random random = new Random();

        for (int j = 0, k = 0; true;) {
            try {
                lock.lockWrite();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            boolean mark = random.nextBoolean();

            if (mark && j < entriesToAppend.length) {
                database.append(entriesToAppend[j]);
                System.out.println("Appended: " + entriesToAppend[j].toString());
                j++;
            } else if (k < entriesToRemove.length) {
                database.remove(entriesToRemove[k]);
                System.out.println("Removed: " + entriesToRemove[k].toString());
                k++;
            } else {
                lock.unlockWrite();
                break;
            }

            lock.unlockWrite();
        }
    }
}
