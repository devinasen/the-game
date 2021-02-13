package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

/** This is the main entry point for the program. This class simply parses
 *  the command line inputs, and lets the byow.Core.Engine class take over
 *  in either keyboard or input string mode.
 */
public class Main {
    public static void main(String[] args) {
//        if (args.length > 1) {
//            System.out.println("Can only have one argument - the input string");
//            System.exit(0);
//        } else if (args.length == 1) {
//            Engine engine = new Engine();
//            engine.interactWithInputString(args[0]);
//            System.out.println(engine.toString());
//        } else {
//            Engine engine = new Engine();
//            engine.interactWithKeyboard();
//        }
        Engine e = new Engine();
        TERenderer ter = new TERenderer();
        ter.initialize(80, 30);
        TETile[][] a, b;
        a = e.interactWithInputString("n205990625849168503sssdawssddaadsaa");
        b = e.interactWithInputString("n205990625849168503sssdawssddaadsaa");

        for (int x = 0; x < a.length; x++) {
            for (int y = 0; y < a[x].length; y++) {
                if (!a[x][y].equals(b[x][y])) {
                    System.out.println("Nooo");
                }
            }
        }
        //ter.renderFrame(a);
        //ter.renderFrame(b);

        //ter.renderFrame(e.interactWithInputString("n205990625849168503sssdawssddaadsaa"));
        //ter.renderFrame(e.interactWithInputString("n4675535341198815800ssaswwssdsawws"));
        //ter.renderFrame(e.interactWithInputString("n4675535341198815800ssaswwssdsaww:q"));
        //ter.renderFrame(e.interactWithInputString("ls"));
        //ter.renderFrame(e.interactWithInputString("n1234sddwsdssaswwwwsd:q"));
        ter.renderFrame(e.interactWithInputString("n1234sddwsds:q"));
        ter.renderFrame(e.interactWithInputString("lsaswww:q"));
        ter.renderFrame(e.interactWithInputString("lwsd"));
    }
}
