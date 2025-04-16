import mayflower.*;

public class TurnGraphic extends Actor {

    public TurnGraphic(boolean T) {
        T = true;
        MayflowerImage turnBlueImg = new MayflowerImage("src/img/turnBoardBlue.png");
        MayflowerImage turnRedImg = new MayflowerImage("src/img/turnBoardRed.png");
        turnBlueImg.scale(201, 144); turnRedImg.scale(201,144);


        if (T == true)
        {
            setImage(turnBlueImg);
        }
        if (T == false)
        {
            setImage(turnRedImg);
        }
    }

    public void act() {

    }

}
