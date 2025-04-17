package jaco.mp3.player;

final class z extends k {
   private int n;
   private float o;
   private float p;
   private float q;

   public z(int var1) {
      super(var1);
   }

   public final void a(H var1, M var2, r var3) {
      super.a(var1, var2, var3);
   }

   public final void a(H var1, r var2) {
      if (this.b != 0) {
         this.c = var1.d(2);
         this.n = var1.d(2);
         if (var2 != null) {
            var2.a(this.c, 2);
            var2.a(this.n, 2);
         }
      }

   }

   public final void a(H var1, M var2) {
      if (this.b != 0) {
         super.a(var1, var2);
         switch(this.n) {
         case 0:
            this.o = m[var1.d(6)];
            this.p = m[var1.d(6)];
            this.q = m[var1.d(6)];
            return;
         case 1:
            this.o = this.p = m[var1.d(6)];
            this.q = m[var1.d(6)];
            return;
         case 2:
            this.o = this.p = this.q = m[var1.d(6)];
            return;
         case 3:
            this.o = m[var1.d(6)];
            this.p = this.q = m[var1.d(6)];
         }
      }

   }

   public final boolean a(H var1) {
      return super.a(var1);
   }

   public final boolean a(int var1, E var2, E var3) {
      if (this.b != 0) {
         float var4 = this.j[this.i];
         if (this.g[0] == null) {
            var4 = (var4 + this.l[0]) * this.k[0];
         }

         if (var1 == 0) {
            float var5 = var4;
            if (this.h <= 4) {
               var4 *= this.d;
               var5 *= this.o;
            } else if (this.h <= 8) {
               var4 *= this.e;
               var5 *= this.p;
            } else {
               var4 *= this.f;
               var5 *= this.q;
            }

            var2.a(var4, this.a);
            var3.a(var5, this.a);
         } else if (var1 == 1) {
            if (this.h <= 4) {
               var4 *= this.d;
            } else if (this.h <= 8) {
               var4 *= this.e;
            } else {
               var4 *= this.f;
            }

            var2.a(var4, this.a);
         } else {
            if (this.h <= 4) {
               var4 *= this.o;
            } else if (this.h <= 8) {
               var4 *= this.p;
            } else {
               var4 *= this.q;
            }

            var2.a(var4, this.a);
         }
      }

      return ++this.i == 3;
   }
}
