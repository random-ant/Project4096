package jaco.mp3.player;

final class w extends Thread {
   // $FF: synthetic field
   private C a;

   w(C var1) {
      this.a = var1;
      super();
   }

   public final void run() {
      this.a.a.skipForward();
   }
}
