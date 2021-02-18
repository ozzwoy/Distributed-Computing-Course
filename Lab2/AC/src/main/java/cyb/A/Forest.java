package cyb.A;

import java.util.Arrays;

public class Forest {
    private final boolean[][] glades;

    public Forest(int size) {
        this.glades = new boolean[size][];
        for (int i = 0; i < size; i++) {
            glades[i] = new boolean[size];
            Arrays.fill(glades[i], false);
        }
    }

    public int getSize() {
        return glades.length;
    }

    public void invitePooh(int i, int j) {
        glades[i][j] = true;
    }

    public boolean checkForPooh(int i, int j) {
        return glades[i][j];
    }

    public void clearGlade(int i, int j) {
        glades[i][j] = false;
    }
}
