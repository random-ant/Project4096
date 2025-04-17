package mayflower.core;

import mayflower.Mayflower;

public class MayflowerThread implements Runnable {
   private Mayflower mayflower;
   private boolean running = false;

   public MayflowerThread(Mayflower mayflower) {
      this.mayflower = mayflower;
   }

   public void start() {
      (new Thread(this)).start();
   }

   public void run() {
      if (!this.running) {
         this.running = true;

         while(this.running) {
            this.mayflower.tick();
            Thread.yield();
         }

      }
   }

   public void stop() {
      this.running = false;
   }
}
