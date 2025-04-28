import mayflower.*;

/**
 * Represents the end screen of the game.
 */
public class EndWorld extends World {

    /**
     * Constructs an EndWorld instance with the specified win state and player color.
     *
     * @param win   Whether the player won or lost.
     * @param color The color of the player.
     */
    public EndWorld(boolean win, BColor color) {
        MayflowerImage img;
        if (win) {
            if (color == BColor.BLUE) {
                img = new MayflowerImage("src/img/blueWin.png");
            } else {
                img = new MayflowerImage("src/img/redWin.png");
            }
        } else {
            if (color == BColor.BLUE) {
                img = new MayflowerImage("src/img/blueLose.png");
            } else {
                img = new MayflowerImage("src/img/redLose.png");
            }
        }
        img.scale(800, 1000);
        setBackground(img);
    }

    public void act() {
    }

}
