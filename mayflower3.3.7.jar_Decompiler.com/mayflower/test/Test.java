package mayflower.test;

import mayflower.Mayflower;

public class Test extends Mayflower {
   public Test() {
      super("Test Mayflower", 800, 600);
   }

   public void init() {
      System.out.println("Init");
      Mayflower.showBounds(true);
      TestWorld w = new TestWorld();
      TestActor2 a = new TestActor2();
      TestActor2 b = new TestActor2();
      w.addObject(a, 200, 200);
      w.addObject(b, 100, 100);
      System.out.println("Mayflower.setWorld(w)");
      Mayflower.setWorld(w);
      Mayflower.setFullScreen(true);
   }

   public static void main(String[] args) {
      new Test();
   }
}
