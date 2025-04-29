package game;

import mayflower.*;

/**
 * Represents the start screen of the game.
 */
public class StartWorld extends World {
    private GameClient client;
    private Game game;

    /**
     * Constructs a {@code StartWorld} instance with the specified client and game.
     * Sets the background to the start screen image.
     *
     * @param client The client associated with the start world.
     * @param game   The game logic associated with the start world.
     */
    public StartWorld(GameClient client, Game game) {
        this.client = client;
        this.game = game;
        MayflowerImage img = new MayflowerImage("src/img/startScreen.png");
        img.scale(800, 1000);
        setBackground(img);
    }

    /**
     * Updates the state of the start world.
     * If the "Enter" key is pressed, sends a "ready" message to the server and
     * updates the background
     * to the ready screen image.
     */
    public void act() {
        if (Mayflower.isKeyDown(Keyboard.KEY_ENTER)) {
            client.send("ready");
            MayflowerImage img = new MayflowerImage("src/img/readyScreen.png");
            img.scale(800, 1000);
            setBackground(img);
        }
    }

    /**
     * Starts the game and transitions to the game world.
     *
     * @return The newly created {@code GameWorld} instance.
     */
    public GameWorld startGame() {
        GameWorld world = new GameWorld(client, game);
        Mayflower.setWorld(world);
        return world;
    }

    /**
     * Sets the game logic for the start world.
     *
     * @param game The game logic to set.
     */
    public void setGame(Game game) {
        this.game = game;
    }
}
