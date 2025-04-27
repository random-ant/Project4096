import mayflower.*;

public class StartWorld extends World {
    GameClient client;
    Game game;

    public StartWorld(GameClient client, Game game) {
        this.client = client;
        this.game = game;
    }

    public void act() {
        if (Mayflower.isKeyDown(Keyboard.KEY_ENTER)) {
            client.send("ready");
            showText("READY", 400, 500);
        }
    }

    public GameWorld startGame() {
        GameWorld world = new GameWorld(client, game);

        BColor player = game.getMyColor();
        Mayflower.setWorld(world);
        return world;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
