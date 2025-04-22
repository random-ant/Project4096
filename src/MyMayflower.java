import mayflower.*;

public class MyMayflower extends Mayflower {
   // public MyMayflower() {
   // this("Game", 800, 1000, new GameWorld()));
   // }
   private GameWorld world;

   public MyMayflower(String title, int width, int height, GameWorld world) {
      super(title, width, height);
      this.world = world;
      Mayflower.setWorld(this.world);
   }

   public void init() {
      Mayflower.setFullScreen(false);
      Mayflower.setWorld(this.world);
   }

}
