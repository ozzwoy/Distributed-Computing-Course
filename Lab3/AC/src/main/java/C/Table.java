package C;

public class Table {
    private boolean tobacco = false;
    private boolean paper = false;
    private boolean matches = false;

    public boolean checkForTobacco() {
        return tobacco;
    }

    public boolean checkForPaper() {
        return paper;
    }

    public boolean checkForMatches() {
        return matches;
    }

    public boolean takeTobacco() {
        if (tobacco) {
            tobacco = false;
            return true;
        }
        return false;
    }

    public boolean takePaper() {
        if (paper) {
            paper = false;
            return true;
        }
        return false;
    }

    public boolean takeMatches() {
        if (matches) {
            matches = false;
            return true;
        }
        return false;
    }

    public void putTobacco() {
        tobacco = true;
    }

    public void putPaper() {
        paper = true;
    }

    public void putMatches() {
        matches = true;
    }
}
