package byow.Core;

import java.util.ArrayList;


public class Avatar {

    private int x, y;
    private int startX, startY;


    private ArrayList<Location> floor;

    public Avatar(int xx, int yy) {
        x = xx;
        y = yy;
        startX = xx;
        startY = yy;

    }

    public Avatar(ArrayList<Location> floorTiles, int item) {
        floor = floorTiles;
        x = floor.get(item).X();
        y = floor.get(item).Y();

    }

    public boolean checkValid(int xx, int yy) {
        for (Location i : floor) {
            if (i.X() == xx && i.Y() == yy) {
                return true;
            }
        }
        return false;
    }

    public void moves(char c) {
        c = Character.toLowerCase(c);

        if (c == 'w') { //up
            if (checkValid(x, y + 1)) {
                y += 1;
            }
        }
        if (c == 's') { //down
            if (checkValid(x, y - 1)) {
                y -= 1;
            }
        }
        if (c == 'a') { //left
            if (checkValid(x - 1, y)) {
                x -= 1;
            }
        }
        if (c == 'd') { //right
            if (checkValid(x + 1, y)) {
                x += 1;
            }
        }
    }

    public Location moves1(char c) {
        c = Character.toLowerCase(c);

        if (c == 'w') { //up
            if (checkValid(x, y + 1)) {
                return new Location(x, y + 1);
            }
        }
        if (c == 's') { //down
            if (checkValid(x, y - 1)) {
                return new Location(x, y - 1);
            }
        }
        if (c == 'a') { //left
            if (checkValid(x - 1, y)) {
                return new Location(x - 1, y);
            }
        }
        if (c == 'd') { //right
            if (checkValid(x + 1, y)) {
                return new Location(x + 1, y);
            }
        }
        return new Location(x, y);
    }

    public Location getMove(char c) {
        return moves1(c);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public ArrayList<Location> getFloor() {
        return floor;
    }

    public void setFloor(ArrayList<Location> floors) {
        floor = floors;
    }
}

