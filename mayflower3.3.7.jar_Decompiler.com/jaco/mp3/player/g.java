package jaco.mp3.player;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Kernel;
import java.util.Hashtable;

public class g extends G {
   public static int a = 1;
   public static int b = 2;
   private Kernel e;
   protected boolean c;
   protected boolean d;
   private int f;

   public g() {
      this(new float[9]);
   }

   private g(float[] var1) {
      this(new Kernel(3, 3, var1));
   }

   private g(Kernel var1) {
      this.e = null;
      this.c = true;
      this.d = true;
      this.f = a;
      this.e = var1;
   }

   public BufferedImage filter(BufferedImage var1, BufferedImage var2) {
      int var3 = var1.getWidth();
      int var4 = var1.getHeight();
      if (var2 == null) {
         var2 = this.createCompatibleDestImage(var1, (ColorModel)null);
      }

      int[] var5 = new int[var3 * var4];
      int[] var6 = new int[var3 * var4];
      boolean var8 = false;
      boolean var7 = false;
      int var12;
      if ((var12 = var1.getType()) != 2 && var12 != 1) {
         var1.getRGB(0, 0, var3, var4, var5, 0, var3);
      } else {
         int[] var10000 = (int[])var1.getRaster().getDataElements(0, 0, var3, var4, var5);
      }

      int var9;
      int var10;
      int var11;
      float var13;
      int[] var28;
      int var32;
      int var33;
      if (this.d) {
         var33 = var5.length;
         var7 = false;
         var28 = var5;
         var33 = var33;

         for(var9 = 0; var9 < var33; ++var9) {
            var11 = (var10 = var28[var9]) >>> 24;
            var12 = var10 >> 16 & 255;
            var32 = var10 >> 8 & 255;
            var10 &= 255;
            var13 = (float)var11 * 0.003921569F;
            var12 = (int)((float)var12 * var13);
            var32 = (int)((float)var32 * var13);
            var10 = (int)((float)var10 * var13);
            var28[var9] = var11 << 24 | var12 << 16 | var32 << 8 | var10;
         }
      }

      Kernel var45 = this.e;
      var12 = this.f;
      boolean var37 = this.c;
      Kernel var29 = var45;
      int var14;
      int var15;
      int var16;
      float var17;
      float var18;
      float var19;
      float var20;
      int var22;
      float var23;
      int var24;
      int var25;
      int var30;
      int var31;
      int[] var36;
      int[] var38;
      float[] var39;
      int var44;
      if (var45.getHeight() == 1) {
         var9 = var12;
         var8 = var37;
         var31 = var4;
         var30 = var3;
         var38 = var6;
         var36 = var5;
         var11 = 0;
         var39 = var29.getKernelData((float[])null);
         var32 = var29.getWidth() / 2;

         for(var14 = 0; var14 < var31; ++var14) {
            var15 = var14 * var30;

            for(var16 = 0; var16 < var30; ++var16) {
               var17 = 0.0F;
               var18 = 0.0F;
               var19 = 0.0F;
               var20 = 0.0F;

               for(var22 = -var32; var22 <= var32; ++var22) {
                  if ((var23 = var39[var32 + var22]) != 0.0F) {
                     if ((var24 = var16 + var22) < 0) {
                        if (var9 == a) {
                           var24 = 0;
                        } else if (var9 == b) {
                           var24 = (var16 + var30) % var30;
                        }
                     } else if (var24 >= var30) {
                        if (var9 == a) {
                           var24 = var30 - 1;
                        } else if (var9 == b) {
                           var24 = (var16 + var30) % var30;
                        }
                     }

                     var25 = var36[var15 + var24];
                     var20 += var23 * (float)(var25 >>> 24);
                     var17 += var23 * (float)(var25 >> 16 & 255);
                     var18 += var23 * (float)(var25 >> 8 & 255);
                     var19 += var23 * (float)(var25 & 255);
                  }
               }

               var22 = var8 ? P.a((int)((double)var20 + 0.5D)) : 255;
               var44 = P.a((int)((double)var17 + 0.5D));
               var24 = P.a((int)((double)var18 + 0.5D));
               var25 = P.a((int)((double)var19 + 0.5D));
               var38[var11++] = var22 << 24 | var44 << 16 | var24 << 8 | var25;
            }
         }
      } else if (var29.getWidth() == 1) {
         var9 = var12;
         var8 = var37;
         var31 = var4;
         var30 = var3;
         var38 = var6;
         var36 = var5;
         var11 = 0;
         var39 = var29.getKernelData((float[])null);
         var32 = var29.getHeight() / 2;

         for(var14 = 0; var14 < var31; ++var14) {
            for(var15 = 0; var15 < var30; ++var15) {
               float var40 = 0.0F;
               var17 = 0.0F;
               var18 = 0.0F;
               var19 = 0.0F;

               int var21;
               int var42;
               for(var42 = -var32; var42 <= var32; ++var42) {
                  if ((var21 = var14 + var42) < 0) {
                     if (var9 == a) {
                        var22 = 0;
                     } else if (var9 == b) {
                        var22 = (var14 + var31) % var31 * var30;
                     } else {
                        var22 = var21 * var30;
                     }
                  } else if (var21 >= var31) {
                     if (var9 == a) {
                        var22 = (var31 - 1) * var30;
                     } else if (var9 == b) {
                        var22 = (var14 + var31) % var31 * var30;
                     } else {
                        var22 = var21 * var30;
                     }
                  } else {
                     var22 = var21 * var30;
                  }

                  if ((var23 = var39[var42 + var32]) != 0.0F) {
                     var24 = var36[var22 + var15];
                     var19 += var23 * (float)(var24 >>> 24);
                     var40 += var23 * (float)(var24 >> 16 & 255);
                     var17 += var23 * (float)(var24 >> 8 & 255);
                     var18 += var23 * (float)(var24 & 255);
                  }
               }

               var42 = var8 ? P.a((int)((double)var19 + 0.5D)) : 255;
               var21 = P.a((int)((double)var40 + 0.5D));
               var22 = P.a((int)((double)var17 + 0.5D));
               var44 = P.a((int)((double)var18 + 0.5D));
               var38[var11++] = var42 << 24 | var21 << 16 | var22 << 8 | var44;
            }
         }
      } else {
         var9 = var12;
         var8 = var37;
         var31 = var4;
         var30 = var3;
         var38 = var6;
         var36 = var5;
         var11 = 0;
         var39 = var29.getKernelData((float[])null);
         var14 = var29.getHeight();
         var32 = var29.getWidth();
         var14 /= 2;
         var15 = var32 / 2;

         for(var16 = 0; var16 < var31; ++var16) {
            for(int var41 = 0; var41 < var30; ++var41) {
               var18 = 0.0F;
               var19 = 0.0F;
               var20 = 0.0F;
               float var43 = 0.0F;

               for(var22 = -var14; var22 <= var14; ++var22) {
                  if ((var44 = var16 + var22) >= 0 && var44 < var31) {
                     var24 = var44 * var30;
                  } else if (var9 == a) {
                     var24 = var16 * var30;
                  } else {
                     if (var9 != b) {
                        continue;
                     }

                     var24 = (var44 + var31) % var31 * var30;
                  }

                  var25 = var32 * (var22 + var14) + var15;

                  for(var44 = -var15; var44 <= var15; ++var44) {
                     float var26;
                     if ((var26 = var39[var25 + var44]) != 0.0F) {
                        int var27;
                        if ((var27 = var41 + var44) < 0 || var27 >= var30) {
                           if (var9 == a) {
                              var27 = var41;
                           } else {
                              if (var9 != b) {
                                 continue;
                              }

                              var27 = (var41 + var30) % var30;
                           }
                        }

                        var27 = var36[var24 + var27];
                        var43 += var26 * (float)(var27 >>> 24);
                        var18 += var26 * (float)(var27 >> 16 & 255);
                        var19 += var26 * (float)(var27 >> 8 & 255);
                        var20 += var26 * (float)(var27 & 255);
                     }
                  }
               }

               var22 = var8 ? P.a((int)((double)var43 + 0.5D)) : 255;
               var44 = P.a((int)((double)var18 + 0.5D));
               var24 = P.a((int)((double)var19 + 0.5D));
               var25 = P.a((int)((double)var20 + 0.5D));
               var38[var11++] = var22 << 24 | var44 << 16 | var24 << 8 | var25;
            }
         }
      }

      if (this.d) {
         var33 = var6.length;
         var7 = false;
         var28 = var6;
         var33 = var33;

         for(var9 = 0; var9 < var33; ++var9) {
            var11 = (var10 = var28[var9]) >>> 24;
            var12 = var10 >> 16 & 255;
            var32 = var10 >> 8 & 255;
            var10 &= 255;
            if (var11 != 0 && var11 != 255) {
               var13 = 255.0F / (float)var11;
               var12 = (int)((float)var12 * var13);
               var32 = (int)((float)var32 * var13);
               var10 = (int)((float)var10 * var13);
               if (var12 > 255) {
                  var12 = 255;
               }

               if (var32 > 255) {
                  var32 = 255;
               }

               if (var10 > 255) {
                  var10 = 255;
               }

               var28[var9] = var11 << 24 | var12 << 16 | var32 << 8 | var10;
            }
         }
      }

      var8 = false;
      var7 = false;
      if ((var12 = var2.getType()) != 2 && var12 != 1) {
         var2.setRGB(0, 0, var3, var4, var6, 0, var3);
      } else {
         var2.getRaster().setDataElements(0, 0, var3, var4, var6);
      }

      return var2;
   }

   public BufferedImage createCompatibleDestImage(BufferedImage var1, ColorModel var2) {
      if (var2 == null) {
         var2 = var1.getColorModel();
      }

      return new BufferedImage(var2, var2.createCompatibleWritableRaster(var1.getWidth(), var1.getHeight()), var2.isAlphaPremultiplied(), (Hashtable)null);
   }

   public Rectangle2D getBounds2D(BufferedImage var1) {
      return new Rectangle(0, 0, var1.getWidth(), var1.getHeight());
   }

   public Point2D getPoint2D(Point2D var1, Point2D var2) {
      if (var2 == null) {
         var2 = new Double();
      }

      ((Point2D)var2).setLocation(var1.getX(), var1.getY());
      return (Point2D)var2;
   }

   public RenderingHints getRenderingHints() {
      return null;
   }

   public String toString() {
      return "Blur/Convolve...";
   }
}
