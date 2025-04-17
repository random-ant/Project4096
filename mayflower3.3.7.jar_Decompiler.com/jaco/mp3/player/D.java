package jaco.mp3.player;

final class D extends x {
   private int j;
   private float k;
   private int l;
   private float n;
   private float o;
   private float p;

   public D(int var1) {
      super(var1);
   }

   public final void a(H var1, M var2, r var3) {
      this.d = var1.d(4);
      this.j = var1.d(4);
      if (var3 != null) {
         var3.a(this.d, 4);
         var3.a(this.j, 4);
      }

      if (this.d != 0) {
         this.f = this.d + 1;
         this.h = a[this.d];
         this.i = b[this.d];
      }

      if (this.j != 0) {
         this.l = this.j + 1;
         this.o = a[this.j];
         this.p = b[this.j];
      }

   }

   public final void a(H var1, M var2) {
      if (this.d != 0) {
         this.e = m[var1.d(6)];
      }

      if (this.j != 0) {
         this.k = m[var1.d(6)];
      }

   }

   public final boolean a(H var1) {
      boolean var2 = super.a(var1);
      if (this.j != 0) {
         this.n = (float)var1.d(this.l);
      }

      return var2;
   }

   public final boolean a(int var1, E var2, E var3) {
      super.a(var1, var2, var3);
      if (this.j != 0 && var1 != 1) {
         float var4 = (this.n * this.o + this.p) * this.k;
         if (var1 == 0) {
            var3.a(var4, this.c);
         } else {
            var2.a(var4, this.c);
         }
      }

      return true;
   }
}
