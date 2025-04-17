package jaco.mp3.player;

final class b extends x {
   private float j;

   public b(int var1) {
      super(var1);
   }

   public final void a(H var1, M var2, r var3) {
      super.a(var1, var2, var3);
   }

   public final void a(H var1, M var2) {
      if (this.d != 0) {
         this.e = m[var1.d(6)];
         this.j = m[var1.d(6)];
      }

   }

   public final boolean a(H var1) {
      return super.a(var1);
   }

   public final boolean a(int var1, E var2, E var3) {
      if (this.d != 0) {
         this.g = this.g * this.h + this.i;
         float var5;
         if (var1 == 0) {
            var5 = this.g * this.e;
            float var4 = this.g * this.j;
            var2.a(var5, this.c);
            var3.a(var4, this.c);
         } else if (var1 == 1) {
            var5 = this.g * this.e;
            var2.a(var5, this.c);
         } else {
            var5 = this.g * this.j;
            var2.a(var5, this.c);
         }
      }

      return true;
   }
}
