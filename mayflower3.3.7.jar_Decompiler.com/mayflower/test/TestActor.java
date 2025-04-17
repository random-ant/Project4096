package mayflower.test;

import mayflower.Actor;
import mayflower.Mayflower;
import mayflower.MayflowerImage;

public class TestActor extends Actor {
   int moveDistance = 1;

   public TestActor() {
      this.setImage(new MayflowerImage("img/karel.png"));
   }

   public void act() {
      if (Mayflower.isKeyPressed(87)) {
         System.out.println("PRESS W");
      }

      if (!Mayflower.isKeyDown(87) && Mayflower.wasKeyDown(87)) {
         System.out.println("RELEASE W");
      }

      if (Mayflower.isKeyPressed(68)) {
         System.out.println("PRESS D");
      }

      if (!Mayflower.isKeyDown(68) && Mayflower.wasKeyDown(68)) {
         System.out.println("RELEASE D");
      }

   }

   public void actX() {
      if (Mayflower.isKeyDown(87)) {
         this.move(1);
      }

      if (Mayflower.isKeyDown(65)) {
         this.turn(-1);
      }

      if (Mayflower.isKeyDown(68)) {
         this.turn(1);
      }

      if (Mayflower.isKeyPressed(32)) {
         this.setLocation(100.0D, 100.0D);
      }

      this.getWorld().showText("R:" + this.getRotation(), 300, 60);
      this.getWorld().showText("X:" + this.getX(), 300, 110);
      this.getWorld().showText("Y:" + this.getY(), 300, 160);
   }
}
