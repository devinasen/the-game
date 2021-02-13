package byow.Core;

public class Location {
    private int xCoord, yCoord;

    public Location(int x, int y) {
        xCoord = x;
        yCoord = y;
    }

    public int X() {
        return xCoord;
    }

    public int Y() {
        return yCoord;
    }

    public void setxCoord(int val) {
        xCoord = val;
    }

    public void setyCoord(int val) {
        yCoord = val;
    }

    public boolean equals(Location coords) {
        if (xCoord == coords.xCoord && yCoord == coords.yCoord) {
            return true;
        }
        return false;
    }
}
