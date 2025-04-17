package mayflower;

public class Color {
   public static final Color BLACK;
   public static final Color BLUE;
   public static final Color CYAN;
   public static final Color DARK_GRAY;
   public static final Color GRAY;
   public static final Color GREEN;
   public static final Color LIGHT_GRAY;
   public static final Color MEGENTA;
   public static final Color ORANGE;
   public static final Color PINK;
   public static final Color RED;
   public static final Color WHITE;
   public static final Color YELLOW;
   private java.awt.Color color;

   static {
      BLACK = new Color(java.awt.Color.black);
      BLUE = new Color(java.awt.Color.blue);
      CYAN = new Color(java.awt.Color.cyan);
      DARK_GRAY = new Color(java.awt.Color.darkGray);
      GRAY = new Color(java.awt.Color.gray);
      GREEN = new Color(java.awt.Color.green);
      LIGHT_GRAY = new Color(java.awt.Color.lightGray);
      MEGENTA = new Color(java.awt.Color.magenta);
      ORANGE = new Color(java.awt.Color.orange);
      PINK = new Color(java.awt.Color.pink);
      RED = new Color(java.awt.Color.red);
      WHITE = new Color(java.awt.Color.white);
      YELLOW = new Color(java.awt.Color.yellow);
   }

   public Color(int r, int g, int b) {
      this.color = new java.awt.Color(r, g, b);
   }

   public Color(int r, int g, int b, int a) {
      this.color = new java.awt.Color(r, g, b, a);
   }

   public Color(java.awt.Color color) {
      this.color = color;
   }

   public Color brighter() {
      return new Color(this.color.brighter());
   }

   public Color darker() {
      return new Color(this.color.darker());
   }

   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      } else {
         return obj instanceof Color ? this.color.equals(((Color)obj).getAwtColor()) : false;
      }
   }

   public int getAlpha() {
      return this.color.getAlpha();
   }

   public int getBlue() {
      return this.color.getBlue();
   }

   public int getGreen() {
      return this.color.getGreen();
   }

   public int getRed() {
      return this.color.getRed();
   }

   public int hashCode() {
      return this.color.hashCode() + 1;
   }

   public String toString() {
      return this.color.toString();
   }

   public java.awt.Color getAwtColor() {
      return this.color;
   }
}
