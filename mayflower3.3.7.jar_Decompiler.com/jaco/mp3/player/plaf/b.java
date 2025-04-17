package jaco.mp3.player.plaf;

import jaco.mp3.player.MP3Player;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final class b implements ActionListener {
   // $FF: synthetic field
   private MP3PlayerUI a;
   // $FF: synthetic field
   private final MP3Player b;

   b(MP3PlayerUI var1, MP3Player var2) {
      this.a = var1;
      this.b = var2;
      super();
   }

   public final void actionPerformed(ActionEvent var1) {
      Object var2;
      if ((var2 = var1.getSource()) == MP3PlayerUI.a(this.a)) {
         this.b.play();
      } else if (var2 == MP3PlayerUI.b(this.a)) {
         this.b.pause();
      } else if (var2 == MP3PlayerUI.c(this.a)) {
         this.b.stop();
      } else if (var2 == MP3PlayerUI.d(this.a)) {
         this.b.skipBackward();
      } else {
         if (var2 == MP3PlayerUI.e(this.a)) {
            this.b.skipForward();
         }

      }
   }
}
