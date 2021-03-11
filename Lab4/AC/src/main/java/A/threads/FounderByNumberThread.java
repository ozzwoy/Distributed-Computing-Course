package A.threads;

import A.database.PhonebookDatabase;
import A.rwlock.CustomReadWriteLock;

public class FounderByNumberThread extends Thread {
    private final PhonebookDatabase database;
    private final CustomReadWriteLock lock;
    private final String[] numbersToFind;

    public FounderByNumberThread(PhonebookDatabase database, CustomReadWriteLock lock, String[] numbersToFind) {
        this.database = database;
        this.lock = lock;
        this.numbersToFind = numbersToFind;
    }

    @Override
    public void run() {
        for (String number : numbersToFind) {
            try {
                lock.lockRead();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Search for " + number + ": " + database.findByNumber(number));
            lock.unlockRead();
        }
    }
}
