package jaco.mp3.player;

class x extends s {
   public static final float[] a = new float[]{0.0F, 0.6666667F, 0.2857143F, 0.13333334F, 0.06451613F, 0.031746034F, 0.015748031F, 0.007843138F, 0.0039138943F, 0.0019550342F, 9.770396E-4F, 4.884005E-4F, 2.4417043E-4F, 1.2207776E-4F, 6.103702E-5F};
   public static final float[] b = new float[]{0.0F, -0.6666667F, -0.8571429F, -0.9333334F, -0.9677419F, -0.98412704F, -0.992126F, -0.9960785F, -0.99804306F, -0.9990225F, -0.9995115F, -0.99975586F, -0.9998779F, -0.99993896F, -0.9999695F};
   protected int c;
   private int j;
   protected int d;
   protected float e;
   protected int f;
   protected float g;
   protected float h;
   protected float i;

   public x(int var1) {
      this.c = var1;
      this.j = 0;
   }

   public void a(H var1, M var2, r var3) {
      if ((this.d = var1.d(4)) == 15) {
         throw new y(514, (Throwable)null);
      } else {
         if (var3 != null) {
            var3.a(this.d, 4);
         }

         if (this.d != 0) {
            this.f = this.d + 1;
            this.h = a[this.d];
            this.i = b[this.d];
         }

      }
   }

   public void a(H var1, M var2) {
      if (this.d != 0) {
         this.e = m[var1.d(6)];
      }

   }

   public boolean a(H var1) {
      if (this.d != 0) {
         this.g = (float)var1.d(this.f);
      }

      if (++this.j == 12) {
         this.j = 0;
         return true;
      } else {
         return false;
      }
   }

   public boolean a(int var1, E var2, E var3) {
      if (this.d != 0 && var1 != 2) {
         float var4 = (this.g * this.h + this.i) * this.e;
         var2.a(var4, this.c);
      }

      return true;
   }
}
