package mayflower.test;

import mayflower.Color;
import mayflower.Mayflower;
import mayflower.MouseInfo;
import mayflower.World;
import mayflower.event.EventListener;

public class TestWorld extends World implements EventListener {
   public TestWorld() {
      this.setBackground("img/800600.png");
      Mayflower.showBounds(true);
   }

   public void act() {
      MouseInfo mi = Mayflower.getMouseInfo();
      this.showText("Test: " + mi.getX() + ", " + mi.getY(), 24, 20, 20, Color.BLACK);
   }

   public void onEvent(String action) {
      System.out.println(action);
   }
}
