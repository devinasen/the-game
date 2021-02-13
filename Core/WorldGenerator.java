package byow.Core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class WorldGenerator {
    // instance variables
    private ArrayList<Room> room; // rooms
    private ArrayList<Hall> hallway; // hallways
    private ArrayList<Location> wall; // walls
    private ArrayList<Location> floor; // floors
    private Random r; // from random class
    private long s; // String input to long

    private int w; //width
    private int h; //height
    private int minSize; // min size
    private int maxSize; // max size
    private int roomMin; // min # of rooms
    private int roomMax; // max # of rooms
    private Location pokemon;
    //private double min = 1000000; // comparing the distance squared


    public WorldGenerator(String seed, int height, int width, int minS,
                          int maxS, int minR, int maxR) {

        for (int i = 0; i < seed.length(); i++) {
            if (!Character.isDigit(seed.charAt(i))) {
                seed = seed.substring(0, i);
                break;
            }
        }
        s = Long.parseLong(seed);
        r = new Random(s);

        room = new ArrayList<>();
        hallway = new ArrayList<>();
        wall = new ArrayList<>();
        floor = new ArrayList<>();

        w = width;
        h = height;
        minSize = minS;
        maxSize = maxS;
        roomMin = minR;
        roomMax = maxR;

    }

    public void makeWorld() {
        roomMaker();    //adds all rooms to instance variable
        build(room);  //sorts the rooms in the shortest path possible to connect
        hallMaker();    //adds all halls necessary to link the rooms instance
        addRooms(room);
        addHalls(hallway);
        pokemon = floor.get(RandomUtils.uniform(r, floor.size()));
    }

    private void addRooms(ArrayList<Room> rooms) {
        for (Room rm : rooms) {
            wall.addAll(rm.getWall());
            floor.addAll(rm.getFloor());
        }
    }

    private void addHalls(ArrayList<Hall> hallways) {
        for (Hall hl : hallways) {   //adds all hall's floors and walls to map instance variables
            if (hl != null) {
                floor.addAll(hl.getFloor());
                wall.addAll(hl.getWall());
            }
        }
    }

    private void roomMaker() {
        int numRooms = roomGen();
        Room[] arrayRooms = new Room[numRooms];
        Location newOrigin;
        int updatedWidth, updatedHeight;
        Room t;

        for (int k = 0; k < numRooms; k++) {

            while (arrayRooms[k] == null) {
                newOrigin = randomLocation();
                updatedWidth = randomWidth();
                updatedHeight = randomHeight();

                t = new Room(newOrigin, updatedWidth, updatedHeight);
                arrayRooms[k] = t;
                for (int j = 0; j < k; j++) {
                    if (!t.intersects(arrayRooms[j])) {
                        continue;
                    } else {
                        arrayRooms[k] = null;
                        break;
                    }
                }
            }
        }
        room.addAll(Arrays.asList(arrayRooms));
    }

    private void hallMaker() {
        int size = room.size();
        Room[] hallRoom = new Room[size];
        for (int i = 0; i < hallRoom.length; i++) {
            hallRoom[i] = room.get(i);
        }
        hallway.addAll(Arrays.asList(Hall.generateHall(hallRoom)));
    }

    public void build(ArrayList<Room> rooms) {
        int size = rooms.size();
        int closestIndex;


        for (int i = 0; i < size; i++) {
            closestIndex = closestNeighbor(i, rooms);
            if (closestIndex == 0) {
                rooms.add(0, rooms.remove(rooms.size() - 1));
            } else if (closestIndex == i + 1 || closestIndex == i - 1) { // no swap if in place
                continue;
            } else {
                Room temp = rooms.get(i + 1);
                rooms.add(i + 1, rooms.remove(closestIndex));
            }
        }
    }

    private int closestNeighbor(int current, ArrayList<Room> rooms) {
        int index = -1;
        double min = 10000000;
        //double min = 625; //max size squared
        Location loc1 = rooms.get(current).center();
        Location loc2;
        int size = rooms.size();
        double distance;

        if (current == size - 1) {
            loc2 = rooms.get(size - 2).center();
            min = Math.pow(loc1.X() - loc2.X(), 2) + Math.pow(loc1.Y() - loc2.Y(), 2);
            index = size - 2;
            loc2 = rooms.get(0).center();

            if (Math.pow(loc1.X() - loc2.X(), 2) + Math.pow(loc1.Y() - loc2.Y(), 2) < min) {
                index = 0;
            }
        } else {
            for (int i = current + 1; i < size; i++) {
                loc2 = rooms.get(i).center();
                distance = Math.pow(loc1.X() - loc2.X(), 2) + Math.pow(loc1.Y() - loc2.Y(), 2);
                if (distance < min) {
                    min = distance;
                    index = i;
                }
            }
        }
        return index;
    }

    //randomVariables room generator
    public int roomGen() {
        return RandomUtils.uniform(r, (roomMax - roomMin) + 1) + roomMin;
    }

    public Location randomLocation() {
        return new Location(RandomUtils.uniform(r, w - maxSize),
                RandomUtils.uniform(r, h - maxSize));
    }

    // randomVariables width generator
    public int randomWidth() {
        return RandomUtils.uniform(r, maxSize - minSize + 1) + minSize;
    }

    // randomVariables height generator
    public int randomHeight() {
        return RandomUtils.uniform(r, maxSize - minSize + 1) + minSize;
    }

    public ArrayList<Room> getRooms() {
        return room;
    }

    public ArrayList<Hall> getHalls() {
        return hallway;
    }

    public ArrayList<Location> getFloor() {
        return floor;
    }

    public Location getPokemon() {
        return pokemon;
    }

    public Random getR() {
        return r;
    }

}

