package byow.Core;

import java.io.Serializable;

public class Saver implements Serializable {

    private String seed;
    private String moves;
    private int x;
    private int y;

    // Default constructor
    public Saver(String input, int xx, int yy) {
        seed = input.substring(1, input.indexOf('S'));
        moves = input.substring(input.indexOf('S'));
        x = xx;
        y = yy;
    }


    public Saver(String s, String m, int xx, int yy) {
        seed = s;
        moves = m;
        x = xx;
        y = yy;
    }

    public String getSeed() {
        return seed;
    }

    public String getMoves() {
        return moves;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
