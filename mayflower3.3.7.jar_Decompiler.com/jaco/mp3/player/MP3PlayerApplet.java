package jaco.mp3.player;

import jaco.mp3.player.plaf.MP3PlayerUICompact;
import java.awt.Color;
import java.net.URL;
import javax.swing.JApplet;

public class MP3PlayerApplet extends JApplet {
   public void init() {
      try {
         try {
            this.getContentPane().setBackground(Color.decode(this.getParameter("background")));
         } catch (Exception var6) {
         }

         if ("true".equals(this.getParameter("compact"))) {
            MP3Player.setDefaultUI(MP3PlayerUICompact.class);
         }

         MP3Player var1;
         (var1 = new MP3Player()).setRepeat(true);
         String[] var5;
         int var4 = (var5 = this.getParameter("playlist").split(",")).length;

         for(int var3 = 0; var3 < var4; ++var3) {
            String var2 = var5[var3];
            var1.addToPlayList(new URL(this.getCodeBase() + var2.trim()));
         }

         this.getContentPane().add(var1);
      } catch (Exception var7) {
         var7.printStackTrace();
      }
   }
}
