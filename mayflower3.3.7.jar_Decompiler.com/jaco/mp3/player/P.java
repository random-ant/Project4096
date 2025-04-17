package jaco.mp3.player;

import java.util.Random;

public final class P {
   static {
      new Random();
   }

   public static int a(int var0) {
      if (var0 < 0) {
         return 0;
      } else {
         return var0 > 255 ? 255 : var0;
      }
   }
}
