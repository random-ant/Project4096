package jaco.mp3.player;

import java.io.PrintStream;

public class t extends Exception {
   private Throwable a;

   public t() {
   }

   public t(String var1, Throwable var2) {
      super(var1);
      this.a = var2;
   }

   public void printStackTrace() {
      this.printStackTrace(System.err);
   }

   public void printStackTrace(PrintStream var1) {
      if (this.a == null) {
         super.printStackTrace(var1);
      } else {
         this.a.printStackTrace();
      }
   }
}
