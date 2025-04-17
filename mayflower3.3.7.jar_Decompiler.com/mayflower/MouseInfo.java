package mayflower;

public class MouseInfo {
   private Actor actor;
   private int button;
   private int clickCount;
   private int x;
   private int y;

   public MouseInfo(Actor actor, int button, int clickCount, int x, int y) {
      this.actor = actor;
      this.button = button;
      this.clickCount = clickCount;
      this.x = x;
      this.y = y;
   }

   public Actor getActor() {
      return this.actor;
   }

   public int getButton() {
      return this.button;
   }

   public int getClickCount() {
      return this.clickCount;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public String toString() {
      return "MouseInfo[actor:" + this.actor + ", button:" + this.button + ",clicks:" + this.clickCount + ", x:" + this.x + ", y:" + this.y + "]";
   }
}
