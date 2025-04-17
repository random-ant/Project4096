package jaco.mp3.player;

import java.net.URL;

final class C extends Thread {
   // $FF: synthetic field
   final MP3Player a;

   C(MP3Player var1) {
      this.a = var1;
   }

   public final void run() {
      c var1 = new c();

      H var2;
      try {
         var2 = new H(((URL)MP3Player.a(this.a).get(MP3Player.b(this.a))).openStream());
      } catch (Exception var8) {
         var2 = null;
         var8.printStackTrace();
      }

      if (var2 != null) {
         R var3;
         try {
            (var3 = new R()).a(var1);
         } catch (Exception var7) {
            var3 = null;

            try {
               var2.a();
            } catch (Exception var6) {
            }

            var7.printStackTrace();
         }

         if (var3 != null) {
            try {
               while(!MP3Player.c(this.a)) {
                  if (MP3Player.d(this.a)) {
                     Thread.sleep(100L);
                  } else {
                     M var4;
                     if ((var4 = var2.b()) == null) {
                        break;
                     }

                     J var10 = (J)var1.a(var4, var2);
                     var3.a(var10.a(), 0, var10.b());
                     var2.d();
                  }
               }
            } catch (Exception var9) {
               var9.printStackTrace();
            }

            if (!MP3Player.c(this.a)) {
               var3.c();
            }

            var3.a();
         }

         try {
            var2.a();
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      }

      if (!MP3Player.c(this.a)) {
         (new w(this)).start();
      }

      MP3Player.a(this.a, false);
      MP3Player.b(this.a, true);
   }
}
