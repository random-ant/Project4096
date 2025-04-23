import mayflower.*;

/**
 * Represents the graphic that displays the current player's turn.
 * This class updates the turn graphic based on the current player's turn.
 */
public class TurnGraphic extends Actor {
    private MayflowerImage turnBlueImg, turnRedImg, turnNeutralImg;

    /**
     * Constructs a TurnGraphic with the specified initial color.
     * Initializes the images for the turn graphic and sets the initial image.
     * 
     * @param color The initial color of the turn graphic.
     */
    public TurnGraphic(BColor color) {
        turnBlueImg = new MayflowerImage("src/img/turnBoardBlue.png");
        turnRedImg = new MayflowerImage("src/img/turnBoardRed.png");
        turnNeutralImg = new MayflowerImage("src/img/turnBoardNeutral.png");
        turnBlueImg.scale(201, 144);
        turnRedImg.scale(201, 144);
        turnNeutralImg.scale(201, 144);

        if (color == BColor.BLUE) {
            setImage(turnBlueImg);
        } else if (color == BColor.RED) {
            setImage(turnRedImg);
        } else {
            setImage(turnNeutralImg);
        }
    }

    /**
     * Updates the display of the turn graphic based on the current player's turn.
     * 
     * @param color The color of the current player's turn.
     */
    public void setTurn(BColor color) {
        if (color == BColor.BLUE) {
            setImage(turnBlueImg);
        } else if (color == BColor.RED) {
            setImage(turnRedImg);
        } else {
            setImage(turnNeutralImg);
        }
    }

    /**
     * Updates the display of the turn graphic based on the current player's turn
     * and whether it is their turn.
     * 
     * @param color  The color of the current player's turn.
     * @param isTurn Whether it is the player's turn.
     */
    public void setTurn(BColor color, boolean isTurn) {
        if (isTurn) {
            setImage(turnBlueImg);
        } else {
            setImage(turnNeutralImg);
        }
    }

    /**
     * Defines the behavior of the TurnGraphic.
     * Currently, this method does not perform any actions.
     */
    public void act() {
    }
}
