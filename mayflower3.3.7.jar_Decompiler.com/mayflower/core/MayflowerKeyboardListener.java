package mayflower.core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import mayflower.Mayflower;

public class MayflowerKeyboardListener implements KeyListener {
   private Mayflower mayflower;

   public MayflowerKeyboardListener(Mayflower mayflower) {
      this.mayflower = mayflower;
   }

   public void keyPressed(KeyEvent e) {
      this.mayflower.keyPressed(e.getKeyCode(), e.getKeyChar());
   }

   public void keyReleased(KeyEvent e) {
      this.mayflower.keyReleased(e.getKeyCode(), e.getKeyChar());
   }

   public void keyTyped(KeyEvent e) {
   }
}
