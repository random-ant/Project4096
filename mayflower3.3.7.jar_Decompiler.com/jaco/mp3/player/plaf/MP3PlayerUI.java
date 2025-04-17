package jaco.mp3.player.plaf;

import jaco.mp3.player.F;
import jaco.mp3.player.MP3Player;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;

public class MP3PlayerUI extends BasicPanelUI {
   private JButton a;
   private JButton b;
   private JButton c;
   private JButton d;
   private JButton e;

   public static ComponentUI createUI(JComponent var0) {
      return new MP3PlayerUI();
   }

   public final void installUI(JComponent var1) {
      super.installUI(var1);
      this.a((MP3Player)var1);
   }

   public final void uninstallUI(JComponent var1) {
      super.uninstallUI(var1);
      MP3Player var2;
      (var2 = (MP3Player)var1).removeAll();
      var2.removeAllMP3PlayerListeners();
   }

   protected void a(MP3Player var1) {
      var1.setOpaque(false);
      this.a = new a(this, F.a(this.getClass().getResource("resources/mp3PlayerPlay.png")));
      this.b = new a(this, F.a(this.getClass().getResource("resources/mp3PlayerPause.png")));
      this.c = new a(this, F.a(this.getClass().getResource("resources/mp3PlayerStop.png")));
      this.d = new a(this, F.a(this.getClass().getResource("resources/mp3PlayerSkipBackward.png")));
      this.e = new a(this, F.a(this.getClass().getResource("resources/mp3PlayerSkipForward.png")));
      b var2 = new b(this, var1);
      this.a.addActionListener(var2);
      this.b.addActionListener(var2);
      this.c.addActionListener(var2);
      this.d.addActionListener(var2);
      this.e.addActionListener(var2);
      var1.setLayout(new FlowLayout(1, 1, 1));
      var1.add(this.a);
      var1.add(this.b);
      var1.add(this.c);
      var1.add(this.d);
      var1.add(this.e);
   }

   // $FF: synthetic method
   static JButton a(MP3PlayerUI var0) {
      return var0.a;
   }

   // $FF: synthetic method
   static JButton b(MP3PlayerUI var0) {
      return var0.b;
   }

   // $FF: synthetic method
   static JButton c(MP3PlayerUI var0) {
      return var0.c;
   }

   // $FF: synthetic method
   static JButton d(MP3PlayerUI var0) {
      return var0.d;
   }

   // $FF: synthetic method
   static JButton e(MP3PlayerUI var0) {
      return var0.e;
   }
}
