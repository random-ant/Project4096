package jaco.mp3.player;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

public final class A extends I {
   private float g = 0.5F;

   public A() {
      this.e = 2.0F;
   }

   public final void a(float var1) {
      this.g = var1;
   }

   public final BufferedImage filter(BufferedImage var1, BufferedImage var2) {
      int var3 = var1.getWidth();
      int var4 = var1.getHeight();
      if (var2 == null) {
         var2 = this.createCompatibleDestImage(var1, (ColorModel)null);
      }

      int[] var5 = new int[var3 * var4];
      int[] var6 = new int[var3 * var4];
      var1.getRGB(0, 0, var3, var4, var5, 0, var3);
      if (this.e > 0.0F) {
         a(this.f, var5, var6, var3, var4, this.c, this.c && this.d, false, a);
         a(this.f, var6, var5, var4, var3, this.c, false, this.c && this.d, a);
      }

      var1.getRGB(0, 0, var3, var4, var6, 0, var3);
      float var16 = 4.0F * this.g;
      int var17 = 0;

      for(int var7 = 0; var7 < var4; ++var7) {
         for(int var8 = 0; var8 < var3; ++var8) {
            int var9;
            int var10 = (var9 = var6[var17]) >> 16 & 255;
            int var11 = var9 >> 8 & 255;
            int var12 = var9 & 255;
            int var13;
            int var14 = (var13 = var5[var17]) >> 16 & 255;
            int var15 = var13 >> 8 & 255;
            var13 &= 255;
            var10 = P.a((int)((float)var10 + var16 * (float)var14));
            var11 = P.a((int)((float)var11 + var16 * (float)var15));
            var12 = P.a((int)((float)var12 + var16 * (float)var13));
            var5[var17] = var9 & -16777216 | var10 << 16 | var11 << 8 | var12;
            ++var17;
         }
      }

      var2.setRGB(0, 0, var3, var4, var5, 0, var3);
      return var2;
   }

   public final String toString() {
      return "Blur/Glow...";
   }
}
