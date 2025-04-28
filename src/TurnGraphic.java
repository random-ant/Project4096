import mayflower.*;

/**
 * Represents the turn indicator graphic in the game.
 */
public class TurnGraphic extends Actor {
    private BColor turn;
    private MayflowerImage turnBlueImg, turnRedImg, turnNeutralImg;

    /**
     * Constructs a TurnGraphic instance with the specified initial turn color.
     *
     * @param color The initial turn color.
     */
    public TurnGraphic(BColor color) {
        turnBlueImg = new MayflowerImage("src/img/turnBoardBlue.png");
        turnRedImg = new MayflowerImage("src/img/turnBoardRed.png");
        turnNeutralImg = new MayflowerImage("src/img/turnBoardNeutral.png");
        turnBlueImg.scale(201, 144);
        turnRedImg.scale(201, 144);
        turnNeutralImg.scale(201, 144);

        this.turn = color;
        updateGraphic();
    }

    /**
     * Updates the display of the turn graphic based on the current turn.
     * 
     * @param blueTurn whether or not it is blue's turn
     * @return void
     */
    public void setTurn(BColor color) {
        this.turn = color;
        updateGraphic();
    }

    private void updateGraphic() {
        if (turn == BColor.BLUE) {
            setImage(turnBlueImg);
        } else if (turn == BColor.RED) {
            setImage(turnRedImg);
        } else {
            setImage(turnNeutralImg);
        }
    }

    public void act() {
    }

}