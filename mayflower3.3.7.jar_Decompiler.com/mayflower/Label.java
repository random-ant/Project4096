package mayflower;

public class Label extends Actor {
   private int point;
   private String oldText;
   private Color color;

   public Label(String text) {
      this(text, 32);
   }

   public Label(String text, int size) {
      this(text, size, Color.WHITE);
   }

   public Label(String text, int size, Color color) {
      this.point = size;
      this.setImage(new MayflowerImage(text, this.point, color));
      this.oldText = text;
      this.color = color;
   }

   public void act() {
   }

   public String getText() {
      return this.oldText;
   }

   public void setText(String text) {
      if (!text.equals(this.oldText)) {
         this.setImage(new MayflowerImage(text, this.point, this.color));
      }

      this.oldText = text;
   }

   public void setColor(Color color) {
      this.color = color;
      String t = this.oldText;
      this.setText("");
      this.setText(t);
   }
}
