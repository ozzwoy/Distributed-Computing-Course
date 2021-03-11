package A.database;

public class FullName {
    public String name;
    public String surname;
    public String patronymic;

    public FullName(String surname, String name, String patronymic) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
    }

    @Override
    public String toString() {
        return surname + " " + name + " " + patronymic;
    }
}
