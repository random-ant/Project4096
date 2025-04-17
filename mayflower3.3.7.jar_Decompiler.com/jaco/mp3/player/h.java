package jaco.mp3.player;

public final class h {
   private final float[] a = new float[32];

   static {
      new h();
   }

   public final void a(h var1) {
      if (var1 != this) {
         float[] var6 = var1.a;
         h var5 = this;
         h var4 = this;

         int var2;
         for(var2 = 0; var2 < 32; ++var2) {
            var4.a[var2] = 0.0F;
         }

         var2 = var6.length > 32 ? 32 : var6.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            float var10002;
            float[] var8;
            label30: {
               var8 = var5.a;
               float var7;
               if ((var7 = var6[var3]) != Float.NEGATIVE_INFINITY) {
                  if (var7 > 1.0F) {
                     var10002 = 1.0F;
                     break label30;
                  }

                  if (var7 < -1.0F) {
                     var10002 = -1.0F;
                     break label30;
                  }
               }

               var10002 = var7;
            }

            var8[var3] = var10002;
         }
      }

   }

   final float[] a() {
      float[] var1 = new float[32];

      for(int var2 = 0; var2 < 32; ++var2) {
         float var3;
         var1[var2] = (var3 = this.a[var2]) == Float.NEGATIVE_INFINITY ? 0.0F : (float)Math.pow(2.0D, (double)var3);
      }

      return var1;
   }
}
