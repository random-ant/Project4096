import mayflower.*;

public class MyMayflower extends Mayflower {

   /**
    * The world to be displayed in the game window.
    */
   private World world;

   /**
    * Constructs a MyMayflower instance with the specified title, dimensions, and
    * world.
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

   /**
    * Initializes the game settings and sets the initial world.
    * This method is called when the game starts.
    */
   public void init() {
      Mayflower.setFullScreen(false);
      Mayflower.setWorld(this.world);
   }
}
