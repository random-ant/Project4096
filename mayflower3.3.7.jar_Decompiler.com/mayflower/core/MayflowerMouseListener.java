package mayflower.core;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import mayflower.Mayflower;

public class MayflowerMouseListener implements MouseListener, MouseMotionListener {
   private int x;
   private int y;
   private Mayflower mayflower;
   private int offX = 3;
   private int offY = 24;

   public MayflowerMouseListener(Mayflower mayflower) {
      this.mayflower = mayflower;
   }

   public void mouseDragged(MouseEvent e) {
      this.mayflower.mouseDragged(this.x, this.y, e.getX() - this.offX, e.getY() - this.offY);
      this.x = e.getX() - this.offX;
      this.y = e.getY() - this.offY;
   }

   public void mouseMoved(MouseEvent e) {
      this.mayflower.mouseMoved(this.x, this.y, e.getX() - this.offX, e.getY() - this.offY);
      this.x = e.getX() - this.offX;
      this.y = e.getY() - this.offY;
   }

   public void mouseClicked(MouseEvent e) {
      this.mayflower.mouseClicked(e.getButton(), e.getX() - this.offX, e.getY() - this.offY, e.getClickCount());
   }

   public void mouseEntered(MouseEvent e) {
   }

   public void mouseExited(MouseEvent e) {
   }

   public void mousePressed(MouseEvent e) {
      this.mayflower.mousePressed(e.getButton(), e.getX() - this.offX, e.getY() - this.offY);
   }

   public void mouseReleased(MouseEvent e) {
      this.mayflower.mouseReleased(e.getButton(), e.getX() - this.offX, e.getY() - this.offY);
   }
}
