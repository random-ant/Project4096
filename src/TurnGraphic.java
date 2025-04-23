import mayflower.*;

public class TurnGraphic extends Actor {
    private boolean blueTurn;
    private MayflowerImage turnBlueImg, turnRedImg;

    public TurnGraphic(boolean blueTurn) {
        this.blueTurn = blueTurn;
        turnBlueImg = new MayflowerImage("src/img/turnBoardBlue.png");
        turnRedImg = new MayflowerImage("src/img/turnBoardRed.png");
        turnBlueImg.scale(201, 144);
        turnRedImg.scale(201, 144);

        if (blueTurn) {
            setImage(turnBlueImg);
        } else {
            setImage(turnRedImg);
        }
    }

    /**
     * Updates the display of the turn graphic based on the current turn.
     * 
     * @param blueTurn whether or not it is blue's turn
     * @return void
     */
    public void setTurn(boolean blueTurn) {
        this.blueTurn = blueTurn;

        if (blueTurn) {
            setImage(turnBlueImg);
        } else {
            setImage(turnRedImg);
        }
    }

    public void act() {
    }

}
