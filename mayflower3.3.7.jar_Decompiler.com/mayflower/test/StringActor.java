package mayflower.test;

import mayflower.Actor;
import mayflower.Color;
import mayflower.MayflowerImage;

public class StringActor extends Actor {
   public StringActor(String message) {
      this.setImage(new MayflowerImage(message, 36, Color.WHITE));
   }

   public void act() {
   }
}
