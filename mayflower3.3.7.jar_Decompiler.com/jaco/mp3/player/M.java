package jaco.mp3.player;

public final class M {
   private static int[][] a = new int[][]{{22050, 24000, 16000, 1}, {44100, 48000, 32000, 1}, {11025, 12000, 8000, 1}};
   private int b;
   private int c;
   private int d;
   private int e;
   private int f;
   private int g;
   private int h;
   private int i;
   private int j;
   private int k;
   private double[] l = new double[]{-1.0D, 384.0D, 1152.0D, 1152.0D};
   private boolean m;
   private int n;
   private int o;
   private byte[] p;
   private byte q = 0;
   private r r;
   private short s;
   private int t;
   private int u;
   private static int[][][] v = new int[][][]{{{0, 32000, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 144000, 160000, 176000, 192000, 224000, 256000, 0}, {0, 8000, 16000, 24000, 32000, 40000, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 144000, 160000, 0}, {0, 8000, 16000, 24000, 32000, 40000, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 144000, 160000, 0}}, {{0, 32000, 64000, 96000, 128000, 160000, 192000, 224000, 256000, 288000, 320000, 352000, 384000, 416000, 448000, 0}, {0, 32000, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 160000, 192000, 224000, 256000, 320000, 384000, 0}, {0, 32000, 40000, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 160000, 192000, 224000, 256000, 320000, 0}}, {{0, 32000, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 144000, 160000, 176000, 192000, 224000, 256000, 0}, {0, 8000, 16000, 24000, 32000, 40000, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 144000, 160000, 0}, {0, 8000, 16000, 24000, 32000, 40000, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 144000, 160000, 0}}};
   private static String[][][] w = new String[][][]{{{"free format", "32 kbit/s", "48 kbit/s", "56 kbit/s", "64 kbit/s", "80 kbit/s", "96 kbit/s", "112 kbit/s", "128 kbit/s", "144 kbit/s", "160 kbit/s", "176 kbit/s", "192 kbit/s", "224 kbit/s", "256 kbit/s", "forbidden"}, {"free format", "8 kbit/s", "16 kbit/s", "24 kbit/s", "32 kbit/s", "40 kbit/s", "48 kbit/s", "56 kbit/s", "64 kbit/s", "80 kbit/s", "96 kbit/s", "112 kbit/s", "128 kbit/s", "144 kbit/s", "160 kbit/s", "forbidden"}, {"free format", "8 kbit/s", "16 kbit/s", "24 kbit/s", "32 kbit/s", "40 kbit/s", "48 kbit/s", "56 kbit/s", "64 kbit/s", "80 kbit/s", "96 kbit/s", "112 kbit/s", "128 kbit/s", "144 kbit/s", "160 kbit/s", "forbidden"}}, {{"free format", "32 kbit/s", "64 kbit/s", "96 kbit/s", "128 kbit/s", "160 kbit/s", "192 kbit/s", "224 kbit/s", "256 kbit/s", "288 kbit/s", "320 kbit/s", "352 kbit/s", "384 kbit/s", "416 kbit/s", "448 kbit/s", "forbidden"}, {"free format", "32 kbit/s", "48 kbit/s", "56 kbit/s", "64 kbit/s", "80 kbit/s", "96 kbit/s", "112 kbit/s", "128 kbit/s", "160 kbit/s", "192 kbit/s", "224 kbit/s", "256 kbit/s", "320 kbit/s", "384 kbit/s", "forbidden"}, {"free format", "32 kbit/s", "40 kbit/s", "48 kbit/s", "56 kbit/s", "64 kbit/s", "80 kbit/s", "96 kbit/s", "112 kbit/s", "128 kbit/s", "160 kbit/s", "192 kbit/s", "224 kbit/s", "256 kbit/s", "320 kbit/s", "forbidden"}}, {{"free format", "32 kbit/s", "48 kbit/s", "56 kbit/s", "64 kbit/s", "80 kbit/s", "96 kbit/s", "112 kbit/s", "128 kbit/s", "144 kbit/s", "160 kbit/s", "176 kbit/s", "192 kbit/s", "224 kbit/s", "256 kbit/s", "forbidden"}, {"free format", "8 kbit/s", "16 kbit/s", "24 kbit/s", "32 kbit/s", "40 kbit/s", "48 kbit/s", "56 kbit/s", "64 kbit/s", "80 kbit/s", "96 kbit/s", "112 kbit/s", "128 kbit/s", "144 kbit/s", "160 kbit/s", "forbidden"}, {"free format", "8 kbit/s", "16 kbit/s", "24 kbit/s", "32 kbit/s", "40 kbit/s", "48 kbit/s", "56 kbit/s", "64 kbit/s", "80 kbit/s", "96 kbit/s", "112 kbit/s", "128 kbit/s", "144 kbit/s", "160 kbit/s", "forbidden"}}};

   M() {
   }

   public final String toString() {
      StringBuffer var1;
      (var1 = new StringBuffer(200)).append("Layer ");
      String var10001;
      switch(this.b) {
      case 1:
         var10001 = "I";
         break;
      case 2:
         var10001 = "II";
         break;
      case 3:
         var10001 = "III";
         break;
      default:
         var10001 = null;
      }

      var1.append(var10001);
      var1.append(" frame ");
      switch(this.h) {
      case 0:
         var10001 = "Stereo";
         break;
      case 1:
         var10001 = "Joint stereo";
         break;
      case 2:
         var10001 = "Dual channel";
         break;
      case 3:
         var10001 = "Single channel";
         break;
      default:
         var10001 = null;
      }

      var1.append(var10001);
      var1.append(' ');
      switch(this.g) {
      case 0:
         var10001 = "MPEG-2 LSF";
         break;
      case 1:
         var10001 = "MPEG-1";
         break;
      case 2:
         var10001 = "MPEG-2.5 LSF";
         break;
      default:
         var10001 = null;
      }

      var1.append(var10001);
      if (this.c != 0) {
         var1.append(" no");
      }

      var1.append(" checksums");
      var1.append(' ');
      switch(this.i) {
      case 0:
         var10001 = this.g == 1 ? "44.1 kHz" : (this.g == 0 ? "22.05 kHz" : "11.025 kHz");
         break;
      case 1:
         var10001 = this.g == 1 ? "48 kHz" : (this.g == 0 ? "24 kHz" : "12 kHz");
         break;
      case 2:
         var10001 = this.g == 1 ? "32 kHz" : (this.g == 0 ? "16 kHz" : "8 kHz");
         break;
      default:
         var10001 = null;
      }

      var1.append(var10001);
      var1.append(',');
      var1.append(' ');
      if (this.m) {
         StringBuilder var8 = new StringBuilder;
         int var9;
         if (!this.m) {
            var9 = v[this.g][this.b - 1][this.d];
         } else {
            float var10003 = (float)(this.o << 3);
            float var10004;
            if (this.m) {
               double var6 = this.l[this.b] / (double)this.e();
               if (this.g == 0 || this.g == 2) {
                  var6 /= 2.0D;
               }

               var10004 = (float)(var6 * 1000.0D);
            } else {
               var10004 = (new float[][]{{8.707483F, 8.0F, 12.0F}, {26.12245F, 24.0F, 36.0F}, {26.12245F, 24.0F, 36.0F}})[this.b - 1][this.i];
            }

            var9 = (int)(var10003 / (var10004 * (float)this.n)) * 1000;
         }

         var8.<init>(String.valueOf(Integer.toString(var9 / 1000)));
         var10001 = var8.append(" kb/s").toString();
      } else {
         var10001 = w[this.g][this.b - 1][this.d];
      }

      var1.append(var10001);
      return var1.toString();
   }

   final void a(H var1, r[] var2) {
      boolean var5 = false;

      int var3;
      do {
         var3 = var1.a(this.q);
         if (this.q == 0) {
            this.g = var3 >>> 19 & 1;
            if ((var3 >>> 20 & 1) == 0) {
               if (this.g != 0) {
                  throw H.b(256);
               }

               this.g = 2;
            }

            if ((this.i = var3 >>> 10 & 3) == 3) {
               throw H.b(256);
            }
         }

         this.b = 4 - (var3 >>> 17) & 3;
         this.c = var3 >>> 16 & 1;
         this.d = var3 >>> 12 & 15;
         this.e = var3 >>> 9 & 1;
         this.h = var3 >>> 6 & 3;
         this.f = var3 >>> 4 & 3;
         if (this.h == 1) {
            this.k = (this.f << 2) + 4;
         } else {
            this.k = 0;
         }

         int var4;
         if (this.b == 1) {
            this.j = 32;
         } else {
            var4 = this.d;
            if (this.h != 3) {
               if (var4 == 4) {
                  var4 = 1;
               } else {
                  var4 -= 4;
               }
            }

            if (var4 != 1 && var4 != 2) {
               if (this.i != 1 && (var4 < 3 || var4 > 5)) {
                  this.j = 30;
               } else {
                  this.j = 27;
               }
            } else if (this.i == 2) {
               this.j = 12;
            } else {
               this.j = 8;
            }
         }

         if (this.k > this.j) {
            this.k = this.j;
         }

         label135: {
            if (this.b == 1) {
               this.t = 12 * v[this.g][0][this.d] / a[this.g][this.i];
               if (this.e != 0) {
                  ++this.t;
               }

               this.t <<= 2;
            } else {
               this.t = 144 * v[this.g][this.b - 1][this.d] / a[this.g][this.i];
               if (this.g == 0 || this.g == 2) {
                  this.t >>= 1;
               }

               if (this.e != 0) {
                  ++this.t;
               }

               if (this.b == 3) {
                  if (this.g == 1) {
                     this.u = this.t - (this.h == 3 ? 17 : 32) - (this.c != 0 ? 0 : 2) - 4;
                  } else {
                     this.u = this.t - (this.h == 3 ? 9 : 17) - (this.c != 0 ? 0 : 2) - 4;
                  }
                  break label135;
               }
            }

            this.u = 0;
         }

         this.t -= 4;
         int var10000 = this.t;
         var4 = var1.c(this.t);
         if (this.t >= 0 && var4 != this.t) {
            throw H.b(261);
         }

         if (var1.a((int)this.q)) {
            if (this.q == 0) {
               this.q = H.b;
               var1.e(var3 & -521024);
            }

            var5 = true;
         } else {
            var1.c();
         }
      } while(!var5);

      var1.e();
      if (this.c == 0) {
         this.s = (short)var1.d(16);
         if (this.r == null) {
            this.r = new r();
         }

         this.r.a(var3, 16);
         var2[0] = this.r;
      } else {
         var2[0] = null;
      }
   }

   final void a(byte[] var1) {
      String var2 = "Xing";
      byte[] var3 = new byte[4];
      byte var4;
      if (this.g == 1) {
         if (this.h == 3) {
            var4 = 17;
         } else {
            var4 = 32;
         }
      } else if (this.h == 3) {
         var4 = 9;
      } else {
         var4 = 17;
      }

      try {
         System.arraycopy(var1, var4, var3, 0, 4);
         if (var2.equals(new String(var3))) {
            this.m = true;
            this.n = -1;
            this.o = -1;
            this.p = new byte[100];
            byte[] var5 = new byte[4];
            System.arraycopy(var1, var4 + 4, var5, 0, var5.length);
            int var8 = 4 + var5.length;
            if ((var5[3] & 1) != 0) {
               System.arraycopy(var1, var4 + var8, var3, 0, var3.length);
               this.n = var3[0] << 24 & -16777216 | var3[1] << 16 & 16711680 | var3[2] << 8 & '\uff00' | var3[3] & 255;
               var8 += 4;
            }

            if ((var5[3] & 2) != 0) {
               System.arraycopy(var1, var4 + var8, var3, 0, var3.length);
               this.o = var3[0] << 24 & -16777216 | var3[1] << 16 & 16711680 | var3[2] << 8 & '\uff00' | var3[3] & 255;
               var8 += 4;
            }

            if ((var5[3] & 4) != 0) {
               System.arraycopy(var1, var4 + var8, this.p, 0, this.p.length);
               var8 += this.p.length;
            }

            if ((var5[3] & 8) != 0) {
               System.arraycopy(var1, var4 + var8, var3, 0, var3.length);
            }
         }
      } catch (ArrayIndexOutOfBoundsException var7) {
         throw new v("XingVBRHeader Corrupted", var7);
      }

      var2 = "VBRI";

      try {
         System.arraycopy(var1, 32, var3, 0, 4);
         if (var2.equals(new String(var3))) {
            this.m = true;
            this.n = -1;
            this.o = -1;
            this.p = new byte[100];
            System.arraycopy(var1, 42, var3, 0, var3.length);
            this.o = var3[0] << 24 & -16777216 | var3[1] << 16 & 16711680 | var3[2] << 8 & '\uff00' | var3[3] & 255;
            System.arraycopy(var1, 46, var3, 0, var3.length);
            this.n = var3[0] << 24 & -16777216 | var3[1] << 16 & 16711680 | var3[2] << 8 & '\uff00' | var3[3] & 255;
         }
      } catch (ArrayIndexOutOfBoundsException var6) {
         throw new v("VBRIVBRHeader Corrupted", var6);
      }
   }

   public final int a() {
      return this.g;
   }

   public final int b() {
      return this.b;
   }

   public final int c() {
      return this.d;
   }

   public final int d() {
      return this.i;
   }

   public final int e() {
      return a[this.g][this.i];
   }

   public final int f() {
      return this.h;
   }

   public final boolean g() {
      return this.s == this.r.a();
   }

   public final int h() {
      return this.u;
   }

   public final int i() {
      return this.f;
   }

   public final int j() {
      return this.j;
   }

   public final int k() {
      return this.k;
   }
}
