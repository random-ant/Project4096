import mayflower.*;

public class MyMayflower extends Mayflower {
   // public MyMayflower() {
   // this("Game", 800, 1000, new GameWorld()));
   // }

   public MyMayflower(String title, int width, int height, World world) {
      super(title, width, height);
      setWorld(world);
   }

   public void init() {
      Mayflower.setFullScreen(false);
   }

}
