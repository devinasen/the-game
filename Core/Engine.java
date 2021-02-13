package byow.Core;

import byow.InputDemo.KeyboardInputSource;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
//import byow.lab13.MemoryGame;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class Engine {
    private TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    private static final int WIDTH = 80;
    private static final int HEIGHT = 30;

    private TETile[][] FINAL_WORLD_FRAME = new TETile[WIDTH][HEIGHT];
    private ArrayList<Location> flooring;
    private String keyInput;
    private String moveSeq;
    private Avatar avatar;
    private Random random;
    private Location pokemon;

    /**
     * Method used for exploring a fresh wg. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        mainMenu();
        KeyboardInputSource key = new KeyboardInputSource();
        char start = key.getNextKey();
        if (start == 'N') {
            keyInput = "";
            StdDraw.clear(Color.BLACK);
            Font font = new Font("Monaco", Font.BOLD, 14);
            StdDraw.setFont(font);
            StdDraw.setPenColor(Color.WHITE);
            String text = "enter seed, end sequence with s";
            StdDraw.text(WIDTH / 2, HEIGHT / 2, text);
            StdDraw.show();
            char seed = key.getNextKey();
            while (true) {
                keyInput += seed;
                if (seed == 'S') {
                    return;
                }
                seed = key.getNextKey();
            }
        }
        if (start == 'L') {
            String filename = "./seed.txt";
            Saver object = null;
            try {
                FileInputStream file = new FileInputStream(filename);
                ObjectInputStream in = new ObjectInputStream(file);
                object = (Saver) in.readObject();
                in.close();
                file.close();
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException ex) {
                System.out.print(filename);
                System.out.println(ex);
                System.out.println("IOException is caught");
                System.exit(0);
            } catch (ClassNotFoundException ex) {
                System.out.println("ClassNotFoundException" + " is caught");
                System.exit(0);
            }
            keyInput = 'L' + object.getSeed();
            moveSeq = object.getMoves();
            avatar = new Avatar(object.getX(), object.getY());
        }
    }


    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same wg state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the wg
     */
    public TETile[][] interactWithInputStringOG(String input) {
        input = input.toLowerCase();
        char i = input.charAt(0);
        String newVal = input.substring(1);
        if (i == 'q') {
            System.exit(0);
            return null; //removed the exit command
        } else if (i == 'l') {
            input = newVal;
            interactHelper(input);
            keyInput += 'S';
            avatar.setFloor(flooring);
        } else {
            input = newVal;
            interactHelper(input);
            avatar = new Avatar(flooring, RandomUtils.uniform(random, flooring.size()));
            StdDraw.filledCircle(avatar.getX(), avatar.getY(), 0.25);
        }
        return FINAL_WORLD_FRAME;
    }

    // input = "N###SDDDD:Q" OR "LDDDDD:Q" OR "Q"
    public TETile[][] interactWithInputString(String input) {
        input = input.toLowerCase();
        char i = input.charAt(0);
        String seed = "";
        String movement = "";

        if (i == 'q') {
            return null;
        } else if (i == 'l') {
            Saver object = loadData();
            seed = object.getSeed();
            movement = input.substring(1);
            avatar = new Avatar(object.getX(), object.getY());
            interactHelper(seed);
            avatar.setFloor(flooring);

        } else {
            seed = input.substring(1, input.indexOf('s'));
            interactHelper(seed);
            avatar = new Avatar(flooring, RandomUtils.uniform(random, flooring.size()));
            movement = input.substring(input.indexOf('s') + 1);

        }
        //FINAL_WORLD_FRAME[avatar.getX()][avatar.getY()] = Tileset.AVATAR;
        //ter.renderFrame(FINAL_WORLD_FRAME);

        char[] directions = movement.toCharArray();

        for (int j = 0; j < directions.length; j++) {
            char c = directions[j];
            if (c == ':') {
                if (j + 1 < directions.length && directions[j + 1] == 'q') {
                    Saver object = new Saver(seed, movement.substring(0, movement.indexOf(":q")),
                            avatar.getX(), avatar.getY());
                    saveData(object);
                }
            }
            FINAL_WORLD_FRAME[avatar.getX()][avatar.getY()] = Tileset.FLOOR;
            avatar.moves(c);
            FINAL_WORLD_FRAME[avatar.getX()][avatar.getY()] = Tileset.AVATAR;
            //ter.renderFrame(FINAL_WORLD_FRAME);
        }
        //FINAL_WORLD_FRAME[avatar.getX()][avatar.getY()] = Tileset.AVATAR;
        return FINAL_WORLD_FRAME;
    }

    private Saver loadData() {
        String filename = "./seed.txt";
        Saver object = null;
        try {
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);
            object = (Saver) in.readObject();
            in.close();
            file.close();
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        } catch (IOException ex) {
            System.out.print(filename);
            System.out.println(ex);
            System.out.println("IOException is caught");
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException" + " is caught");
        }
        return object;
    }

    private void saveData(Saver object) {
        String filename = "./seed.txt";
        try {
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(object);
            out.close();
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            //System.exit(0);
        } catch (IOException ex) {
            System.out.println("IOException is caught");
        }
    }


    private void interactHelper(String input) {
        int maxRoom = 25;
        int minRoom = 4;
        int maxSize = 10;
        int minSize = 5;
        WorldGenerator w = new WorldGenerator(input, HEIGHT, WIDTH, minSize,
                maxSize, minRoom, maxRoom);
        w.makeWorld();
        random = w.getR();
        ArrayList<Room> rooms = w.getRooms();
        ArrayList<Hall> halls = w.getHalls();
        ArrayList<Location> walls = new ArrayList<>();
        flooring = new ArrayList<>();
        addRooms(rooms, walls, flooring, halls);
        addTiles(walls, flooring);
        addPokemon(w.getPokemon());
    }

    private void addRooms(ArrayList<Room> rooms, ArrayList<Location> walls,
                          ArrayList<Location> floor, ArrayList<Hall> halls) {
        for (Room room : rooms) {
            walls.addAll(room.getWall());
            floor.addAll(room.getFloor());
        }

        for (Hall hall : halls) {
            if (hall == null) {
                continue;
            }
            walls.addAll(hall.getWall());
            floor.addAll(hall.getFloor());
        }
    }

    private void addPokemon(Location pokLoc) {
        pokemon = pokLoc;
        FINAL_WORLD_FRAME[pokLoc.X()][pokLoc.Y()] = Tileset.FLOWER;
    }

    private void addTiles(ArrayList<Location> walls, ArrayList<Location> floor) {
        for (Location wall : walls) {
            FINAL_WORLD_FRAME[wall.X()][wall.Y()] = Tileset.WALL;
        }
        for (Location fl : floor) {
            FINAL_WORLD_FRAME[fl.X()][fl.Y()] = Tileset.FLOOR;
        }

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {

                if (FINAL_WORLD_FRAME[i][j] == Tileset.WALL
                        && (((i - 1 >= 0 && FINAL_WORLD_FRAME[i - 1][j] == Tileset.FLOOR)
                        && (i + 1 < WIDTH && FINAL_WORLD_FRAME[i + 1][j] == Tileset.FLOOR))
                        || ((j - 1 >= 0 && FINAL_WORLD_FRAME[i][j - 1] == Tileset.FLOOR)
                        && (j + 1 < HEIGHT && FINAL_WORLD_FRAME[i][j + 1] == Tileset.FLOOR)))) {
                    FINAL_WORLD_FRAME[i][j] = Tileset.FLOOR;
                    flooring.add(new Location(i, j));
                }
                if (FINAL_WORLD_FRAME[i][j] == null) {
                    FINAL_WORLD_FRAME[i][j] = Tileset.NOTHING;
                }
            }
        }
    }

    public void update(KeyboardInputSource k) {
        char move = k.getNextKey();
        if (move == ':') {
            quit(k);
        }
        keyInput += move;
        avatar.moves(move);

        StdDraw.enableDoubleBuffering();
        StdDraw.clear();
        ter.renderFrame(FINAL_WORLD_FRAME);
        StdDraw.setPenColor(Color.YELLOW);
        StdDraw.filledCircle(avatar.getX(), avatar.getY(), 0.25);
        StdDraw.show();
    }

    private void quit(KeyboardInputSource k) {
        char next = k.getNextKey();
        if (next == 'Q') {
            Saver object = new Saver(keyInput, avatar.getX(), avatar.getY());
            String filename = "./seed.txt";
            // Serialization
            try {
                FileOutputStream file = new FileOutputStream(filename);
                ObjectOutputStream out = new ObjectOutputStream(file);
                out.writeObject(object);
                out.close();
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException ex) {
                System.out.println("IOException is caught");
            }
            System.exit(0);
        } else {
            keyInput += next; // for saving
            avatar.moves(next);
            StdDraw.enableDoubleBuffering();
            StdDraw.clear();
            ter.renderFrame(FINAL_WORLD_FRAME);
            StdDraw.setPenColor(Color.YELLOW);
            StdDraw.filledCircle(avatar.getX(), avatar.getY(), 0.25);
            StdDraw.show();
        }
    }


    public void hud(int mouseX, int mouseY) {
        StdDraw.enableDoubleBuffering();
        if (mouseX >= WIDTH) {
            mouseX = WIDTH - 1;
        }
        if (mouseY >= HEIGHT) {
            mouseY = HEIGHT - 1;
        }

        TETile tet = FINAL_WORLD_FRAME[mouseX][mouseY];
        String text = tet.description();
        Font font = new Font("Monaco", Font.BOLD, 14);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.filledRectangle(WIDTH / 5, HEIGHT - 1, 10, 1);
        StdDraw.setPenColor(Color.GREEN);
        StdDraw.text(WIDTH / 5, HEIGHT - 1, text);
        StdDraw.text(WIDTH / 5, HEIGHT - 3, time());
        StdDraw.show();
    }

    private void mainMenu() {
        StdDraw.clear(Color.BLACK);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        String menu1 = "New game (N) Load (L) Quit (Q) ";
        StdDraw.text(WIDTH / 2, HEIGHT * 3 / 4, "CS61B: THE GAME");
        StdDraw.text(WIDTH / 2, HEIGHT / 2, menu1);
        StdDraw.show();
    }

    private String time() {
        Calendar calendar = Calendar.getInstance();
        long t = System.currentTimeMillis();
        calendar.setTimeInMillis(t);
        return calendar.get(Calendar.MONTH) + 1 + "/"
                + calendar.get(Calendar.DAY_OF_MONTH) + "/"
                + calendar.get(Calendar.YEAR) + " "
                + calendar.get(Calendar.HOUR) + ":"
                + calendar.get(Calendar.MINUTE);
    }

    public int currX() {
        return avatar.getX();
    }

    public int currY() {
        return avatar.getY();
    }

    public String getKeyInput() {
        return keyInput;
    }

//    public static void main(String[] args) {
//        Engine e = new Engine();
//        TERenderer ter = new TERenderer();
//        ter.initialize(WIDTH, HEIGHT);
//        e.interactWithKeyboard();
//        ter.renderFrame(e.interactWithInputStringOG(e.getKeyInput()));
//        System.out.println(e.getKeyInput());
//
//        KeyboardInputSource k = new KeyboardInputSource();
//        StdDraw.enableDoubleBuffering();
//        StdDraw.setPenColor(Color.YELLOW);
//        StdDraw.filledCircle(e.currX(), e.currY(), 0.25);
//        StdDraw.show();
//
//        int mouseXOG = (int) Math.floor(StdDraw.mouseX());
//        int mouseYOG = (int) Math.floor(StdDraw.mouseY());
//
//        while (true) {
//            int mouseX = (int) Math.floor(StdDraw.mouseX());
//            int mouseY = (int) Math.floor(StdDraw.mouseY());
//
//            if (mouseXOG != mouseX || mouseYOG != mouseY) {
//                e.hud(mouseX, mouseY);
//            }
//            if (StdDraw.hasNextKeyTyped()) {
//                e.update(k);
//                if (e.pokemon.equals(new Location(e.currX(), e.currY()))) {
//                    MemoryGame mini = new MemoryGame(WIDTH, HEIGHT, e.getKeyInput());
//                    mini.startGame();
//
//                    StdDraw.enableDoubleBuffering();
//                    StdDraw.clear();
//                    ter.renderFrame(FINAL_WORLD_FRAME);
//                    StdDraw.setPenColor(Color.YELLOW);
//                    StdDraw.filledCircle(e.currX(), e.currY(), 0.25);
//                    StdDraw.show();
//                }
//            }
//        }
//    }
}


