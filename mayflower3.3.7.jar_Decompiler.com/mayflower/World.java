package mayflower;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class World {
   private List<Actor> actors;
   private MayflowerImage background;
   private List<Class<? extends Actor>> paintOrder;
   private List<Class<? extends Actor>> actOrder;
   private int cellSize;
   private Map<String, World.TextInfo> texts;
   private Map<String, Font> fonts;
   private Font font;

   public World(int cellSize) {
      this.fonts = new HashMap();
      this.actors = new ArrayList();
      this.setPaintOrder();
      this.setActOrder();
      this.cellSize = cellSize;
      this.texts = new HashMap();
      this.fonts = new HashMap();
      this.font = this.loadFont("Comic Sans MS", 32);
   }

   public World() {
      this(1);
   }

   public abstract void act();

   public void render(Graphics2D g) {
   }

   public void addObject(Actor object, int x, int y) {
      if (object != null) {
         if (object.getWorld() != null) {
            object.getWorld().removeObject(object);
         }

         object.setLocation((double)x, (double)y);
         this.actors.add(object);
         object.addedToWorld(this);
      }
   }

   public MayflowerImage getBackground() {
      return this.background;
   }

   public int getCellSize() {
      return this.cellSize;
   }

   public Color getColorAt(int x, int y) {
      return this.background == null ? Color.BLACK : this.background.getColorAt(x, y);
   }

   public int getHeight() {
      return Mayflower.getHeight();
   }

   public List<Actor> getObjects() {
      return this.actors;
   }

   public <A extends Actor> List<A> getObjects(Class<A> cls) {
      List<A> ret = new LinkedList();

      for(int i = 0; i < this.actors.size(); ++i) {
         Actor actor = (Actor)this.actors.get(i);
         if (cls.isAssignableFrom(actor.getClass())) {
            ret.add(actor);
         }
      }

      return ret;
   }

   public List<Actor> getObjectsAt(int x, int y) {
      List<Actor> ret = new LinkedList();
      List<Actor> arr = this.getObjects();

      for(int i = 0; i < arr.size(); ++i) {
         Actor actor = (Actor)arr.get(i);
         if (actor.getBounds().contains((double)(x * this.cellSize), (double)(y * this.cellSize))) {
            ret.add(actor);
         }
      }

      return ret;
   }

   public <A extends Actor> List<A> getObjectsAt(int x, int y, Class<A> cls) {
      List<A> ret = new LinkedList();
      List<A> objects = this.getObjects(cls);

      for(int i = 0; i < objects.size(); ++i) {
         A actor = (Actor)objects.get(i);
         if (actor.getBounds().contains((double)(x * this.cellSize), (double)(y * this.cellSize))) {
            ret.add(actor);
         }
      }

      return ret;
   }

   public int getWidth() {
      return Mayflower.getWidth();
   }

   public int numberOfObjects() {
      return this.actors.size();
   }

   public void removeObject(Actor object) {
      if (this.actors.remove(object)) {
         object.addedToWorld((World)null);
      }

   }

   public void removeObjects(Collection<? extends Actor> objects) {
      Object[] arr = objects.toArray();
      Object[] var6 = arr;
      int var5 = arr.length;

      for(int var4 = 0; var4 < var5; ++var4) {
         Object obj = var6[var4];
         this.removeObject((Actor)obj);
      }

   }

   public void showText(String text, int x, int y) {
      this.showText(text, 32, x, y);
   }

   public void showText(String text, int size, int x, int y) {
      this.showText(text, size, x, y, Color.WHITE);
   }

   public void showText(String text, int size, int x, int y, Color color) {
      String key = x + "," + y;
      Font font = this.loadFont(this.font.getName(), size);
      this.texts.put(key, new World.TextInfo(text, x, y, font, color));
   }

   public void showText(String text, int x, int y, Color color) {
      String key = x + "," + y;
      this.texts.put(key, new World.TextInfo(text, x, y, this.font, color));
   }

   public void removeText(int x, int y) {
      String key = x + "," + y;
      this.texts.remove(key);
   }

   private Font loadFont(String name, int size) {
      String key = name + "_" + size;
      Font font = (Font)this.fonts.get(key);
      if (font == null) {
         System.out.println("Loading font: " + key);
         font = new Font(name, size);
         this.fonts.put(key, font);
      }

      return font;
   }

   public void setFont(String name, int size) {
      this.font = this.loadFont(name, size);
   }

   public void repaint() {
   }

   public void setActOrder(Class... classes) {
      this.actOrder = new LinkedList();

      try {
         Class[] var5 = classes;
         int var4 = classes.length;

         for(int var3 = 0; var3 < var4; ++var3) {
            Class cls = var5[var3];
            this.actOrder.add(cls);
         }
      } catch (Exception var6) {
         System.out.println(var6);
      }

      this.actOrder.add(Actor.class);
   }

   public void setBackground(MayflowerImage img) {
      this.background = img;
   }

   public void setBackground(String filename) {
      this.setBackground(new MayflowerImage(filename));
   }

   public void setPaintOrder(Class... classes) {
      this.paintOrder = new LinkedList();

      try {
         Class[] var5 = classes;
         int var4 = classes.length;

         for(int var3 = 0; var3 < var4; ++var3) {
            Class cls = var5[var3];
            this.paintOrder.add(cls);
         }
      } catch (Exception var6) {
         System.out.println(var6);
      }

      this.paintOrder.add(Actor.class);
   }

   public void started() {
   }

   public void stopped() {
   }

   public Map<String, World.TextInfo> getTexts() {
      return this.texts;
   }

   protected List<Class<? extends Actor>> getPaintOrder() {
      return this.paintOrder;
   }

   protected List<Class<? extends Actor>> getActOrder() {
      return this.actOrder;
   }

   class TextInfo {
      private String text;
      private int size;
      private int x;
      private int y;
      private Color color;
      private Font font;

      public TextInfo(String text, int x, int y, Font font, Color color) {
         this.x = x;
         this.y = y;
         this.color = color;
         this.text = text;
         this.font = font;
      }

      public Font getFont() {
         return this.font;
      }

      public String getText() {
         return this.text;
      }

      public int getX() {
         return this.x;
      }

      public int getY() {
         return this.y;
      }

      public int getSize() {
         return this.size;
      }

      public Color getColor() {
         return this.color;
      }
   }
}
