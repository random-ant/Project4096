import java.util.Scanner;

import mayflower.*;
import mayflower.net.*;

public class GameClient extends Client {
    private GameWorld world;
    private Game game;

    public GameClient() {
        // Scanner in = new Scanner(System.in);
        // System.out.println("Use localhost to connect to a server running on your computer.");
        // System.out.print("IP Address > ");
        // String ip = in.next();

        // System.out.print("Port > ");
        // int port = in.nextInt();
        int port = 1234; // default server port

        System.out.println("Connecting...");
        // connect(ip, port);
        connect("localhost", port);

    }

    public void process(String message) {
        // "youare RED/BLUE"
        // "move UP/DOWN/LEFT/RIGHT"
        // "addblock row col value"

        System.out.println("Message from server: " + message);

        String[] parts = message.split(" ");

        if ("youare".equals(parts[0])) {
            BColor player = "BLUE".equals(parts[1]) ? BColor.BLUE : BColor.RED;

            game = new Game();

            world = new GameWorld(this, game, player);

            new MyMayflower("game", 800, 1000, world);

            world.renderGrid();

        } else if ("move".equals(parts[0])) {
            String dir = parts[1];
            if ("UP".equals(dir)) {
                game.merge(Direction.UP);
            } else if ("DOWN".equals(dir)) {
                game.merge(Direction.DOWN);
            } else if ("LEFT".equals(dir)) {
                game.merge(Direction.LEFT);
            } else if ("RIGHT".equals(dir)) {
                game.merge(Direction.RIGHT);
            }
            game.nextPlayer();
            world.renderGrid();
        } else if ("addblock".equals(parts[0])) {
            int row = Integer.parseInt(parts[1]);
            int col = Integer.parseInt(parts[2]);
            int val = Integer.parseInt(parts[3]);
            game.addBlock(row, col, val, BColor.NEUTRAL);
        } else if ("error".equals(parts[0])) {
            System.out.println(message);
        } else if ("winner".equals(parts[0])) {
            System.out.println("Opponent disconnected. You win!");
        }

    }

    public void onDisconnect(String message) {
        System.out.println("Disconnected from server.");
    }

    public void onConnect() {
        System.out.println("Connected!");
    }
}
