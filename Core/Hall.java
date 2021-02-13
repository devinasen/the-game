package byow.Core;

import java.util.ArrayList;

/* @Source: A friend who took this course a previous semester helped us figure out how to
 * connect the hallways to rooms
 *
 * @Source: Berkeley CAEE tutor helped design the class structure */

public class Hall {

    private ArrayList<Location> wall;
    private ArrayList<Location> floor;
    private Location start, end;

    public Hall(Location one, Location two) {
        start = one;
        end = two;
        wall = new ArrayList<>();
        floor = new ArrayList<>();

        floor.addAll(fillMe(start, end));
        walls(start, end);
    }

    private static ArrayList<Location> fillMe(Location a, Location b) {
        ArrayList<Location> fillThis = new ArrayList<>();
        int y, x;
        if (a.X() == b.X()) {
            if (b.Y() > a.Y()) {
                y = 1;
            } else {
                y = -1;
            }
            for (int i = 0; i <= Math.abs(b.Y() - a.Y()); i++) {
                fillThis.add(new Location(a.X(), a.Y() + i * y));
            }
        } else {
            if (b.X() > a.X()) {
                x = 1;
            } else {
                x = -1;
            }
            for (int j = 0; j <= Math.abs(b.X() - a.X()); j++) {
                fillThis.add(new Location(a.X() + j * x, a.Y()));
            }
        }
        return fillThis;
    }

    private void floors(Location a, Location b) {
        floor.addAll(fillMe(a, b));
    }

    private void walls(Location a, Location b) {
        int x, y;
        if (a.X() == b.X()) {
            x = 1;
            y = 0;
        } else {
            y = 1;
            x = 0;
        }

        Location w1 = new Location(a.X() + x, a.Y() + y);
        Location w2 = new Location(b.X() + x, b.Y() + y);
        wall.addAll(fillMe(w1, w2));


        w1 = new Location(a.X() - x, a.Y() - y);
        w2 = new Location(b.X() - x, b.Y() - y);
        wall.addAll(fillMe(w1, w2));

    }

    public static byow.Core.Hall[] generateHall(Room[] room) {
        byow.Core.Hall[] hall = new byow.Core.Hall[room.length - 1];
        for (int i = 0; i < room.length - 1; i++) {
            Room previous = room[i];
            Room curr = room[i + 1];
            hall[i] = connect(previous, curr);
        }
        return hall;
    }

    public static byow.Core.Hall connect(Room a, Room b) {
        Location[] r1 = a.getLocations();
        Location[] r2 = b.getLocations();
        Location center1 = new Location(Math.round(a.center().X()), Math.round(a.center().Y()));
        Location center2 = new Location(Math.round(b.center().X()), Math.round(b.center().Y()));

        if (r1[0].X() > r2[0].X() || r1[0].Y() > r2[0].Y()) {
            Location mid = new Location(center2.X(), center1.Y());
            byow.Core.Hall hall = new byow.Core.Hall(center1, mid);
            hall.walls(hall.end, center2);
            hall.floors(hall.end, center2);
            hall.end = center2;
            return hall;
        } else {
            Location mid2 = new Location(center1.X(), center2.Y());
            byow.Core.Hall hall2 = new byow.Core.Hall(center1, mid2);
            hall2.walls(hall2.end, center2);
            hall2.floors(hall2.end, center2);
            hall2.end = center2;
            return hall2;
        }
    }


    public ArrayList<Location> getFloor() {
        return floor;
    }

    public ArrayList<Location> getWall() {
        return wall;
    }

}



