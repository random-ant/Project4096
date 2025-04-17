package jaco.mp3.player.plaf;

import jaco.mp3.player.N;
import jaco.mp3.player.p;
import java.awt.image.BufferedImage;

final class e implements N {
   // $FF: synthetic field
   private MP3PlayerUICompact a;
   // $FF: synthetic field
   private final BufferedImage b;
   // $FF: synthetic field
   private final BufferedImage c;
   // $FF: synthetic field
   private final BufferedImage d;
   // $FF: synthetic field
   private final BufferedImage e;
   // $FF: synthetic field
   private final BufferedImage f;
   // $FF: synthetic field
   private final BufferedImage g;

   e(MP3PlayerUICompact var1, BufferedImage var2, BufferedImage var3, BufferedImage var4, BufferedImage var5, BufferedImage var6, BufferedImage var7) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
      this.d = var4;
      this.e = var5;
      this.f = var6;
      this.g = var7;
      super();
   }

   public final void a() {
      MP3PlayerUICompact.a(this.a).setIcon(p.a(this.b));
      MP3PlayerUICompact.a(this.a).setRolloverIcon(p.a(this.c));
      MP3PlayerUICompact.a(this.a).setPressedIcon(p.a(this.d));
   }

   public final void b() {
      MP3PlayerUICompact.a(this.a).setIcon(p.a(this.e));
      MP3PlayerUICompact.a(this.a).setRolloverIcon(p.a(this.f));
      MP3PlayerUICompact.a(this.a).setPressedIcon(p.a(this.g));
   }
}
