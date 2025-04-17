package mayflower.core;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import javax.swing.JPanel;
import mayflower.Mayflower;

public class MayflowerPanel extends JPanel {
   private Mayflower mayflower;
   private static final long serialVersionUID = 3644893585199489381L;

   public MayflowerPanel(Mayflower mayflower) {
      this.mayflower = mayflower;
   }

   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      BufferedImage buffer = this.mayflower.getBuffer();
      if (buffer != null) {
         g.drawImage(buffer, 0, 0, (ImageObserver)null);
      }

   }
}
