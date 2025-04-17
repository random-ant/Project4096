package mayflower.test;

import mayflower.Actor;

public class TestActor2 extends Actor {
   public TestActor2() {
      this.setImage("rsrc/tiles.jpg", 0, 0, 32, 32);
   }

   public void onEvent(String action) {
      System.out.println("Banana");
   }

   public void act() {
   }
}
