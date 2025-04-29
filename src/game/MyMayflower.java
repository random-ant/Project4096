package game;

import mayflower.*;

/**
 * Custom implementation of the Mayflower game engine.
 */
public class MyMayflower extends Mayflower {
   private World world;

   /**
    * Constructs a MyMayflower instance with the specified parameters.
    *
    * @param title  The title of the game window.
    * @param width  The width of the game window.
    * @param height The height of the game window.
    * @param world  The initial world to display.
    */
   public MyMayflower(String title, int width, int height, World world) {
      super(title, width, height);
      this.world = world;
      Mayflower.setWorld(this.world);
   }

   @Override
   public void init() {
      Mayflower.setFullScreen(false);
      Mayflower.setWorld(this.world);
   }
}
