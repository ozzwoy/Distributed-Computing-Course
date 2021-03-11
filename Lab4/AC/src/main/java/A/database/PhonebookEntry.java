package A.database;

public class PhonebookEntry {
    public FullName fullName;
    public String phoneNumber;

    public PhonebookEntry(FullName fullName, String phoneNumber) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return fullName.toString() + " - " + phoneNumber;
    }
}
