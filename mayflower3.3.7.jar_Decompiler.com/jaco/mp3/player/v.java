package jaco.mp3.player;

public final class v extends t {
   private int a;

   public v(String var1, Throwable var2) {
      super(var1, var2);
      this.a = 256;
   }

   public v(int var1, Throwable var2) {
      this("Bitstream errorcode " + Integer.toHexString(var1), var2);
      this.a = var1;
   }

   public final int a() {
      return this.a;
   }
}
