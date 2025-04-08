import mayflower.*;

public class MyMayflower extends Mayflower {
   public MyMayflower() {
      super("Epic Game", 800, 600);
   }

   public void init() {
      Mayflower.setFullScreen(false);
      // Mayflower.setWorld(new StartScreen());
   }
}
