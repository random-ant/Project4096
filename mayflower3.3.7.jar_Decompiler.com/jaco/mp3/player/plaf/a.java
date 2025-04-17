package jaco.mp3.player.plaf;

import jaco.mp3.player.F;
import jaco.mp3.player.p;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.JButton;

final class a extends JButton {
   private a(MP3PlayerUI var1, BufferedImage var2, byte var3) {
      BufferedImage var5 = F.a(var2, 0.05F);
      BufferedImage var4 = F.b(var2, 0.05F);
      this.setIcon(p.a(var2));
      this.setRolloverIcon(p.a(var5));
      this.setPressedIcon(p.a(var4));
      this.setCursor(Cursor.getPredefinedCursor(12));
      this.setBorder(BorderFactory.createEmptyBorder());
      this.setMargin(new Insets(0, 0, 0, 0));
      this.setContentAreaFilled(false);
      this.setFocusable(false);
      this.setFocusPainted(false);
   }

   // $FF: synthetic method
   a(MP3PlayerUI var1, BufferedImage var2) {
      this(var1, var2, (byte)0);
   }
}
