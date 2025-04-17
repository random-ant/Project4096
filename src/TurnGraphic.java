import mayflower.*;

public class TurnGraphic extends Actor {

    public TurnGraphic(boolean blueTurn) {
        blueTurn = true;
        MayflowerImage turnBlueImg = new MayflowerImage("src/img/turnBoardBlue.png");
        MayflowerImage turnRedImg = new MayflowerImage("src/img/turnBoardRed.png");
        turnBlueImg.scale(201, 144);
        turnRedImg.scale(201, 144);

        if (blueTurn) {
            setImage(turnBlueImg);
        } else {
            setImage(turnRedImg);
        }
    }

    public void act() {

    }

}
