import mayflower.*;

public class TurnGraphic extends Actor {
    private MayflowerImage turnBlueImg, turnRedImg, turnNeutralImg;

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
     * Updates the display of the turn graphic based on the current turn.
     * 
     * @param blueTurn whether or not it is blue's turn
     * @return void
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

    public void setTurn(BColor color, boolean isTurn) {
        if (isTurn) {
            setImage(turnBlueImg);
        } else {
            setImage(turnNeutralImg);
        }
    }

    public void act() {
    }

}
