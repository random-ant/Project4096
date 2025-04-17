package jaco.mp3.player.plaf;

import jaco.mp3.player.MP3Player;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final class d implements ActionListener {
   // $FF: synthetic field
   private final MP3Player a;

   d(MP3PlayerUICompact var1, MP3Player var2) {
      this.a = var2;
   }

   public final void actionPerformed(ActionEvent var1) {
      if (!this.a.isPaused() && !this.a.isStopped()) {
         this.a.pause();
      } else {
         this.a.play();
      }
   }
}
