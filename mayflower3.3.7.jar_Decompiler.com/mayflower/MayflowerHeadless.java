package mayflower;

public abstract class MayflowerHeadless extends Mayflower {
   public MayflowerHeadless(String title, int width, int height) {
      super(title, width, height);
   }

   public void initGUI(String title) {
      System.out.println("No GUI Loaded");
      this.init();
   }

   public void render() {
   }
}
