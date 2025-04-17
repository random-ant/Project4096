package jaco.mp3.player;

final class j {
   private int a = 0;
   private int b = 0;
   private int c = 0;
   private final int[] d = new int['耀'];

   public final int a() {
      return this.b;
   }

   public final int a(int var1) {
      this.b += var1;
      int var2 = 0;
      int var3;
      if ((var3 = this.c) + var1 < 32768) {
         while(var1-- > 0) {
            var2 = (var2 <<= 1) | (this.d[var3++] != 0 ? 1 : 0);
         }
      } else {
         while(var1-- > 0) {
            var2 = (var2 <<= 1) | (this.d[var3] != 0 ? 1 : 0);
            var3 = var3 + 1 & 32767;
         }
      }

      this.c = var3;
      return var2;
   }

   public final int b() {
      ++this.b;
      int var1 = this.d[this.c];
      this.c = this.c + 1 & 32767;
      return var1;
   }

   public final void b(int var1) {
      int var2 = this.a;
      this.d[var2++] = var1 & 128;
      this.d[var2++] = var1 & 64;
      this.d[var2++] = var1 & 32;
      this.d[var2++] = var1 & 16;
      this.d[var2++] = var1 & 8;
      this.d[var2++] = var1 & 4;
      this.d[var2++] = var1 & 2;
      this.d[var2++] = var1 & 1;
      if (var2 == 32768) {
         this.a = 0;
      } else {
         this.a = var2;
      }
   }

   public final void c(int var1) {
      this.b -= var1;
      this.c -= var1;
      if (this.c < 0) {
         this.c += 32768;
      }

   }

   public final void d(int var1) {
      var1 = 4096 << 3;
      this.b -= var1;
      this.c -= var1;
      if (this.c < 0) {
         this.c += 32768;
      }

   }
}
