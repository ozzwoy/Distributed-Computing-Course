package A.threads;

import A.database.FullName;
import A.database.PhonebookDatabase;
import A.database.PhonebookEntry;
import A.rwlock.CustomReadWriteLock;

public class FounderByNameThread extends Thread {
    private final PhonebookDatabase database;
    private final CustomReadWriteLock lock;
    private final FullName[] namesToFind;

    public FounderByNameThread(PhonebookDatabase database, CustomReadWriteLock lock, FullName[] namesToFind) {
        this.database = database;
        this.lock = lock;
        this.namesToFind = namesToFind;
    }

    @Override
    public void run() {
        for (FullName name : namesToFind) {
            try {
                lock.lockRead();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Search for " + name.toString() + ": " + database.findByName(name));
            lock.unlockRead();
        }
    }
}
