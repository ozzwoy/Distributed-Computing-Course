package C;

public class Cigarette {

    public Cigarette(boolean tobacco, boolean paper, boolean matches) {
        if (!tobacco || !paper || !matches) {
            System.out.println("Error! Some ingredient is missing!");
        }
    }

    public void smoke() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
