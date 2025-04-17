package mayflower.test;

import mayflower.Actor;
import mayflower.MayflowerImage;
import mayflower.Timer;

public class FollowerActor extends Actor {
   private Actor target;
   private Timer t;

   public FollowerActor(Actor target) {
      this.target = target;
      this.setImage(new MayflowerImage("rsrc/moblin.png"));
      this.t = new Timer(500);
   }

   public void act() {
      if (this.t.isDone()) {
         if (this.target != null) {
            this.turnTowards(this.target);
         }

         this.move(32);
         this.t.reset();
      }

   }
}
