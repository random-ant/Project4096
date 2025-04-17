package jaco.mp3.player;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.DataLine.Info;

public final class R extends a {
   private SourceDataLine a = null;
   private AudioFormat b = null;
   private byte[] c = new byte[4096];

   protected final void b() {
      if (this.a != null) {
         this.a.close();
      }

   }

   protected final void b(short[] var1, int var2, int var3) {
      if (this.a == null) {
         R var4 = this;
         Object var5 = null;

         try {
            if (var4.b == null) {
               c var7 = var4.e();
               var4.b = new AudioFormat((float)var7.a(), 16, var7.b(), true, false);
            }

            AudioFormat var6 = var4.b;
            Line var12;
            if ((var12 = AudioSystem.getLine(new Info(SourceDataLine.class, var6))) instanceof SourceDataLine) {
               var4.a = (SourceDataLine)var12;
               var4.a.open(var4.b);
               var4.a.start();
            }
         } catch (RuntimeException var8) {
            var5 = var8;
         } catch (LinkageError var9) {
            var5 = var9;
         } catch (LineUnavailableException var10) {
            var5 = var10;
         }

         if (this.a == null) {
            throw new t("cannot obtain source audio line", (Throwable)var5);
         }
      }

      byte[] var11 = this.c(var1, var2, var3);
      this.a.write(var11, 0, var3 << 1);
   }

   private byte[] c(short[] var1, int var2, int var3) {
      int var4 = var3 << 1;
      if (this.c.length < var4) {
         this.c = new byte[var4 + 1024];
      }

      byte[] var6 = this.c;

      short var5;
      for(var4 = 0; var3-- > 0; var6[var4++] = (byte)(var5 >>> 8)) {
         var5 = var1[var2++];
         var6[var4++] = (byte)var5;
      }

      return var6;
   }

   protected final void d() {
      if (this.a != null) {
         this.a.drain();
      }

   }
}
