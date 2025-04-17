package mayflower;

import java.awt.GraphicsEnvironment;

public class Font {
   private java.awt.Font font;
   private String name;
   private boolean bold;
   private boolean italic;
   private int size;

   public static String[] getAvailableFonts() {
      String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
      String[] fontNames = new String[fonts.length];

      for(int i = 0; i < fonts.length; ++i) {
         fontNames[i] = fonts[i].toString();
      }

      return fontNames;
   }

   public Font(boolean bold, boolean italic, int size) {
      this("Comic Sans MS", bold, italic, size);
   }

   public Font(int size) {
      this("Comic Sans MS", false, false, size);
   }

   public Font(String name, boolean bold, boolean italic, int size) {
      int modifiers = 0;
      if (bold) {
         modifiers |= 1;
      }

      if (italic) {
         modifiers |= 2;
      }

      this.font = new java.awt.Font(name, modifiers, size);
      this.name = name;
      this.bold = bold;
      this.italic = italic;
      this.size = size;
   }

   public Font(String name, int size) {
      this(name, false, false, size);
   }

   public Font deriveFont(float size) {
      return new Font(this.name, this.bold, this.italic, (int)size);
   }

   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      } else {
         return obj instanceof Font ? this.font.equals(((Font)obj).getAwtFont()) : false;
      }
   }

   public String getName() {
      return this.name;
   }

   public int getSize() {
      return this.size;
   }

   public int hashCode() {
      return this.font.hashCode() + 1;
   }

   public boolean isBold() {
      return this.bold;
   }

   public boolean isItalic() {
      return this.italic;
   }

   public boolean isPlain() {
      return !this.bold && !this.italic;
   }

   public String toString() {
      return this.font.toString();
   }

   public java.awt.Font getAwtFont() {
      return this.font;
   }
}
