package jaco.mp3.player;

public final class y extends t {
   private y(String var1, Throwable var2) {
      super(var1, var2);
   }

   public y(int var1, Throwable var2) {
      this("Decoder errorcode " + Integer.toHexString(var1), var2);
   }
}
