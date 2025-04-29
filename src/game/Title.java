package game;

 

import mayflower.*;

/**
 * Represents the title image of 4096.
 */
public class Title extends Actor {

    public Title() {
        MayflowerImage title = new MayflowerImage("src/img/title.png");
        title.scale(201, 144);
        setImage(title);
    }

    public void act() {
    }

}
