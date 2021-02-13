package byow.Core;

import java.util.ArrayList;

public class Room {

    private Location o;
    private int w, h;
    private Location[] locations;
    private ArrayList<Location> wall;
    private ArrayList<Location> floor;

    public Room(Location o, int w, int h) {
        this.o = o;
        this.w = w;
        this.h = h;

        locations = new Location[4];
        locations[0] = o;

        locations[1] = new Location(o.X(), o.Y() + h);
        locations[2] = new Location(o.X() + w, o.Y() + h);
        locations[3] = new Location(o.X() + w, o.Y());

        wall = new ArrayList<>();
        floor = new ArrayList<>();

        int x = o.X();
        int y = o.Y();

        for (int i = 0; i < h; i++) { // vertical
            Location l1 = new Location(x, y + i);
            wall.add(l1);
            Location l2 = new Location(x + w - 1, y + i);
            wall.add(l2);
        }

        for (int i = 0; i < w; i++) { // horizontal
            Location l3 = new Location(x + i, y);
            wall.add(l3);
            Location l4 = new Location(x + i, y + h - 1);
            wall.add(l4);
        }

        int z = o.X();
        int q = o.Y();

        for (int j = 1; j < w - 1; j++) {
            for (int i = 1; i < h - 1; i++) {
                Location l5 = new Location(z + j, q + i);
                floor.add(l5);
            }
        }

    }

    public boolean intersects(Room room) {
        Location[] pointArr = room.getLocations();
        int lowerX = locations[0].X();
        int topY = locations[1].Y();
        int lowerY = locations[2].Y();
        int topX = locations[3].X();

        for (int i = 0; i < 4; i++) {
            if (pointArr[i].Y() <= topY && pointArr[i].Y() >= lowerY) {
                if (pointArr[i].X() <= topX && pointArr[i].X() >= lowerX) {
                    return true;
                }
            }
        }
        return false;
    }


    public Location center() {
        int x = w / 2 + o.X();
        int y = h / 2 + o.Y();
        return new Location(x, y);
    }

    public ArrayList<Location> getWall() {
        return wall;
    }

    public ArrayList<Location> getFloor() {
        return floor;
    }

    public Location[] getLocations() {
        return locations;
    }

}

