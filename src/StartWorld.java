import mayflower.*;

/**
 * Represents the starting world of the game, where players wait to begin.
 * This world handles player readiness and transitions to the main game world.
 */
public class StartWorld extends World {

    /**
     * The client associated with this world.
     */
    GameClient client;

    /**
     * The game instance associated with this world.
     */
    Game game;

    /**
     * Constructs a StartWorld with the specified client and game instance.
     * 
     * @param client The GameClient associated with this world.
     * @param game   The Game instance associated with this world.
     */
    public StartWorld(GameClient client, Game game) {
        this.client = client;
        this.game = game;
    }

    /**
     * Defines the behavior of the StartWorld.
     * Checks if the Enter key is pressed to signal player readiness.
     */
    public void act() {
        if (Mayflower.isKeyDown(Keyboard.KEY_ENTER)) {
            client.send("ready");
            showText("READY", 400, 500);
        }
    }

    /**
     * Starts the game and transitions to the GameWorld.
     * 
     * @return The GameWorld instance that represents the main game world.
     */
    public GameWorld startGame() {
        GameWorld world = new GameWorld(client, game);

        BColor player = game.getMyColor();
        if (player == BColor.BLUE)
            world.getTurnGraph().setTurn(BColor.BLUE);
        else
            world.getTurnGraph().setTurn(BColor.NEUTRAL);

        Mayflower.setWorld(world);
        return world;
    }

    /**
     * Sets the game instance for this world.
     * 
     * @param game The Game instance to set.
     */
    public void setGame(Game game) {
        this.game = game;
    }
}
