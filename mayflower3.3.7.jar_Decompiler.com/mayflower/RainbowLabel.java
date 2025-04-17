package mayflower;

public class RainbowLabel extends Label {
   private int r;
   private int b;
   private int g;
   private Timer timer;
   private int speed;

   public RainbowLabel(String text, int size) {
      this(text, size, Color.WHITE);
   }

   public RainbowLabel(String text, int size, Color color) {
      super(text, size, color);
      this.r = 0;
      this.g = 0;
      this.b = 0;
      this.speed = 10;
      this.timer = new Timer(this.speed);
   }

   public void speedUp(int amnt) {
      this.speed -= amnt;
      if (this.speed < 1) {
         this.speed = 1;
      }

      this.timer = new Timer(this.speed);
   }

   public void slowDown(int amnt) {
      this.speedUp(-amnt);
   }

   public void act() {
      if (this.timer.isDone()) {
         this.timer.reset();
         ++this.r;
         if (this.r > 255) {
            this.r = 0;
            ++this.g;
            if (this.g > 255) {
               this.g = 0;
               ++this.b;
               if (this.b > 255) {
                  this.b = 0;
               }
            }
         }

         this.setColor(new Color(this.r, this.g, this.b));
      }
   }
}
