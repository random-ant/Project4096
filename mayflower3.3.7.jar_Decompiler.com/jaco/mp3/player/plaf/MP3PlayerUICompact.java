package jaco.mp3.player.plaf;

import jaco.mp3.player.F;
import jaco.mp3.player.MP3Player;
import jaco.mp3.player.p;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

public class MP3PlayerUICompact extends MP3PlayerUI {
   private JButton a;

   public static ComponentUI createUI(JComponent var0) {
      return new MP3PlayerUICompact();
   }

   protected final void a(MP3Player var1) {
      var1.setOpaque(false);
      BufferedImage var2;
      BufferedImage var3 = F.a(var2 = F.a(this.getClass().getResource("resources/mp3PlayerSoundOn.png")), 0.05F);
      BufferedImage var4 = F.b(var2, 0.05F);
      BufferedImage var5;
      BufferedImage var6 = F.a(var5 = F.a(this.getClass().getResource("resources/mp3PlayerSoundOff.png")), 0.05F);
      BufferedImage var7 = F.b(var5, 0.05F);
      this.a = new JButton();
      this.a.setCursor(Cursor.getPredefinedCursor(12));
      this.a.setBorder(BorderFactory.createEmptyBorder());
      this.a.setMargin(new Insets(0, 0, 0, 0));
      this.a.setContentAreaFilled(false);
      this.a.setFocusable(false);
      this.a.setFocusPainted(false);
      this.a.setIcon(p.a(var5));
      this.a.setRolloverIcon(p.a(var6));
      this.a.setPressedIcon(p.a(var7));
      this.a.addActionListener(new d(this, var1));
      var1.addMP3PlayerListener(new e(this, var2, var3, var4, var5, var6, var7));
      var1.add(this.a);
      var1.setLayout(new c(this));
   }

   // $FF: synthetic method
   static JButton a(MP3PlayerUICompact var0) {
      return var0.a;
   }
}
