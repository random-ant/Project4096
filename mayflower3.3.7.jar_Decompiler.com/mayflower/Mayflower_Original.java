package mayflower;

import jaco.mp3.player.MP3Player;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;
import mayflower.core.MayflowerKeyboardListener;
import mayflower.core.MayflowerMouseListener;
import mayflower.core.MayflowerPanel;
import mayflower.core.MayflowerThread;

public abstract class Mayflower_Original {
   public static final String version = "3.0";
   private static World world;
   private static Set<Integer> pressedKeys;
   private static Set<Integer> wasPressedKeys;
   private static int lastKeyPressed;
   private static int width;
   private static int height;
   private static boolean showBounds;
   private static Shape bounds;
   private static Map<String, Sound> sounds;
   private static Map<String, MP3Player> musics;
   private static boolean fullscreen;
   private static int mouseX;
   private static int mouseY;
   private static int mouseButton = -1;
   private static boolean mouseMoved;
   private static boolean mousePressed;
   private static boolean lastMousePressed;
   private static Map<String, Mayflower_Original.MouseClick> clicks;
   private static Map<String, Mayflower_Original.MouseClick> pastClicks;
   private static boolean paused;
   private static JFrame frame;
   private static MayflowerPanel panel;
   private static MayflowerKeyboardListener keyboardListener;
   private static MayflowerMouseListener mouseListener;
   private static MayflowerThread thread;
   private static BufferedImage buffer;
   private static BufferedImage textLayer;
   private long lastTick;
   private long tickWait;

   public Mayflower_Original(String title, int width, int height) {
      this(title, width, height, true);
   }

   public Mayflower_Original(String title, int width, int height, boolean syncfps) {
      this.lastTick = 0L;
      this.tickWait = 16393442L;
      Mayflower_Original.width = width;
      Mayflower_Original.height = height;
      paused = false;
      pressedKeys = new HashSet();
      wasPressedKeys = new HashSet();
      sounds = new HashMap();
      musics = new HashMap();
      clicks = new HashMap();
      pastClicks = new HashMap();
      bounds = new Rectangle(-1, -1, width + 3, height + 3);
      this.initGUI(title);
      thread.start();
   }

   public void initGUI(String title) {
      TinySound.init();
      frame = new JFrame(title);
      frame.getContentPane().add(panel, "Center");
      frame.addKeyListener(keyboardListener);
      frame.addMouseListener(mouseListener);
      frame.addMouseMotionListener(mouseListener);
      this.init();
      frame.setDefaultCloseOperation(3);
      frame.setSize(width, height);
      frame.setResizable(false);
      frame.setVisible(true);
   }

   public abstract void init();

   public void tick() {
      long now = getTime();
      long var10000 = now - this.lastTick;
      if (0L == this.lastTick || this.lastTick + this.tickWait < now) {
         this.lastTick = now;
         this.update();
         this.render();
      }

   }

   public void render() {
      if (world != null) {
         textLayer = new BufferedImage(width, height, 2);
         BufferedImage buffer = new BufferedImage(width, height, 2);
         Graphics2D g = buffer.createGraphics();
         MayflowerImage background = world.getBackground();
         if (background != null) {
            g.drawImage(background.getBufferedImage(), 0, 0, (ImageObserver)null);
         }

         Set<Actor> painted = new HashSet();
         Iterator var6 = world.getPaintOrder().iterator();

         Iterator var8;
         while(var6.hasNext()) {
            Class<? extends Actor> drawClass = (Class)var6.next();
            var8 = world.getObjects(drawClass).iterator();

            while(var8.hasNext()) {
               Actor actor = (Actor)var8.next();
               if (!painted.contains(actor)) {
                  painted.add(actor);
                  actor.draw(g);
                  if (showBounds) {
                     Shape s = actor.getBounds();
                     g.setColor(java.awt.Color.BLACK);
                     g.draw(s);
                  }
               }
            }
         }

         Graphics2D g2d = (Graphics2D)textLayer.getGraphics();
         Map<String, World.TextInfo> texts = world.getTexts();
         var8 = texts.keySet().iterator();

         while(var8.hasNext()) {
            String key = (String)var8.next();
            World.TextInfo info = (World.TextInfo)texts.get(key);
            g2d.setColor(info.getColor().getAwtColor());
            g2d.setFont(info.getFont().getAwtFont());
            g2d.drawString(info.getText(), info.getX(), info.getY());
         }

         g.drawImage(textLayer, 0, 0, (ImageObserver)null);
         Mayflower_Original.buffer = buffer;
         panel.repaint();
      }
   }

   public BufferedImage getBuffer() {
      return buffer;
   }

   public void update() {
      if (world != null) {
         if (fullscreen && isKeyDown(27)) {
            setFullScreen(false);
         }

         world.act();
         if (!paused) {
            Set<Actor> acted = new HashSet();
            Iterator var3 = world.getActOrder().iterator();

            while(var3.hasNext()) {
               Class<? extends Actor> actClass = (Class)var3.next();
               Iterator var5 = world.getObjects(actClass).iterator();

               while(var5.hasNext()) {
                  Actor actor = (Actor)var5.next();
                  if (actor.getWorld() != null && !acted.contains(actor)) {
                     acted.add(actor);
                     actor.act();
                  }
               }
            }
         }

         wasPressedKeys.clear();
         wasPressedKeys.addAll(pressedKeys);
         pastClicks.clear();
         Iterator var7 = clicks.keySet().iterator();

         while(var7.hasNext()) {
            String key = (String)var7.next();
            pastClicks.put(key, (Mayflower_Original.MouseClick)clicks.get(key));
         }

         clicks.clear();
         mouseMoved = false;
         lastMousePressed = mousePressed;
         mousePressed = false;
      }
   }

   public void keyPressed(int key, char c) {
      pressedKeys.add(key);
      lastKeyPressed = key;
   }

   public void keyReleased(int key, char c) {
      pressedKeys.remove(key);
   }

   public void mousePressed(int button, int x, int y) {
      mousePressed = true;
      mouseButton = button;
      mouseX = x;
      mouseY = y;
   }

   public void mouseReleased(int button, int x, int y) {
      mousePressed = false;
      mouseButton = -1;
   }

   public void mouseDragged(int oldX, int oldy, int newx, int newy) {
   }

   public void mouseClicked(int button, int x, int y, int clickCount) {
      String key = x + " " + y + " " + button;
      Mayflower_Original.MouseClick info = (Mayflower_Original.MouseClick)clicks.get(key);
      if (info == null) {
         clicks.put(key, new Mayflower_Original.MouseClick(x, y, button, clickCount));
      } else {
         info.addClicks(clickCount);
      }

   }

   public void mouseMoved(int oldx, int oldy, int newx, int newy) {
      mouseX = newx;
      mouseY = newy;
      mouseMoved = true;
   }

   public static Shape getBounds() {
      return bounds;
   }

   public static void showBounds(boolean showBounds) {
      Mayflower_Original.showBounds = showBounds;
   }

   public static void showFPS(boolean showFPS) {
   }

   public static int getWidth() {
      return width;
   }

   public static int getHeight() {
      return height;
   }

   public static String ask(String prompt) {
      return JOptionPane.showInputDialog(prompt);
   }

   public static void delay(int time) {
      Timer t = new Timer(time);

      while(!t.isDone()) {
         Thread.yield();
      }

   }

   public static int getKey() {
      return lastKeyPressed;
   }

   public static MouseInfo getMouseInfo() {
      Actor act = null;
      if (world != null) {
         List<Actor> acts = world.getObjectsAt(mouseX, mouseY);
         if (acts.size() > 0) {
            act = (Actor)acts.get(0);
         }
      }

      return new MouseInfo(act, mouseButton, mouseButton >= 0 ? 1 : 0, mouseX, mouseY);
   }

   public static int getRandomNumber(int limit) {
      return (int)(Math.random() * (double)limit);
   }

   public static boolean isKeyDown(int keyName) {
      return pressedKeys.contains(keyName);
   }

   public static boolean wasKeyDown(int keyName) {
      return wasPressedKeys.contains(keyName);
   }

   public static boolean isKeyPressed(int keyName) {
      return isKeyDown(keyName) && !wasKeyDown(keyName);
   }

   public static boolean mouseClicked(Object obj) {
      Collection<Mayflower_Original.MouseClick> clicks = Mayflower_Original.clicks.values();
      if (obj == null) {
         return clicks.size() > 0;
      } else {
         Mayflower_Original.MouseClick click;
         Iterator var4;
         if (obj instanceof World) {
            World world = (World)obj;
            var4 = clicks.iterator();

            while(var4.hasNext()) {
               click = (Mayflower_Original.MouseClick)var4.next();
               if (world.getObjectsAt(click.getX(), click.getY()).size() > 0) {
                  return false;
               }
            }

            return true;
         } else {
            if (obj instanceof Actor) {
               Actor actor = (Actor)obj;
               var4 = clicks.iterator();

               while(var4.hasNext()) {
                  click = (Mayflower_Original.MouseClick)var4.next();
                  if (actor.getBounds().contains((double)click.getX(), (double)click.getY())) {
                     return true;
                  }
               }
            }

            return false;
         }
      }
   }

   public static boolean wasMouseClicked(Object obj) {
      Collection<Mayflower_Original.MouseClick> clicks = pastClicks.values();
      if (obj == null) {
         return clicks.size() > 0;
      } else {
         Mayflower_Original.MouseClick click;
         Iterator var4;
         if (obj instanceof World) {
            World world = (World)obj;
            var4 = clicks.iterator();

            while(var4.hasNext()) {
               click = (Mayflower_Original.MouseClick)var4.next();
               if (world.getObjectsAt(click.getX(), click.getY()).size() > 0) {
                  return false;
               }
            }

            return true;
         } else {
            if (obj instanceof Actor) {
               Actor actor = (Actor)obj;
               var4 = clicks.iterator();

               while(var4.hasNext()) {
                  click = (Mayflower_Original.MouseClick)var4.next();
                  if (actor.getBounds().contains((double)click.getX(), (double)click.getY())) {
                     return true;
                  }
               }
            }

            return false;
         }
      }
   }

   public static boolean mouseDragEnded(Object obj) {
      System.out.println("mouseDragEnded is not implemented yet");
      return false;
   }

   public static boolean mouseDragged(Object obj) {
      System.out.println("mouseDragged is not implemented yet");
      return false;
   }

   public static boolean mousePressed(Object obj) {
      if (mousePressed && !lastMousePressed) {
         if (obj == null) {
            return true;
         }

         if (obj instanceof World) {
            if (((World)obj).getObjectsAt(mouseX, mouseY).size() == 0) {
               return true;
            }

            return false;
         }

         if (obj instanceof Actor) {
            return ((Actor)obj).getBounds().contains((double)mouseX, (double)mouseY);
         }
      }

      return false;
   }

   public static long getTime() {
      return System.nanoTime();
   }

   public static boolean mouseMoved(Object obj) {
      if (!mouseMoved) {
         return false;
      } else if (obj == null) {
         return true;
      } else if (obj instanceof World) {
         return ((World)obj).getObjectsAt(mouseX, mouseY).size() == 0;
      } else {
         return obj instanceof Actor ? ((Actor)obj).getBounds().contains((double)mouseX, (double)mouseY) : false;
      }
   }

   public static void loadSound(String soundFile) {
      File file = new File(soundFile);
      if (file.exists()) {
         Sound sound = TinySound.loadSound(file);
         sounds.put(soundFile, sound);
      }

   }

   public static void loadMusic(String musicFile) {
      File file = new File(musicFile);
      if (file.exists()) {
         MP3Player player = new MP3Player(new File[]{file});
         musics.put(musicFile, player);
      }

   }

   public static void playSound(String soundFile) {
      Sound sound = (Sound)sounds.get(soundFile);
      if (sound == null) {
         loadSound(soundFile);
         sound = (Sound)sounds.get(soundFile);
      }

      if (sound != null) {
         sound.play();
      }

   }

   public static void stopMusic(String musicFile) {
      MP3Player music = (MP3Player)musics.get(musicFile);
      if (music != null) {
         music.stop();
      }

   }

   public static void playMusic(String musicFile) {
      MP3Player music = (MP3Player)musics.get(musicFile);
      if (music == null) {
         loadMusic(musicFile);
         music = (MP3Player)musics.get(musicFile);
      }

      if (music != null) {
         try {
            music.play();
         } catch (Exception var3) {
         }
      }

   }

   public static void setSpeed(int speed) {
      System.out.println("Setting speed doesn't do anything...");
   }

   public static void setWorld(World world) {
      if (Mayflower_Original.world != null) {
         Mayflower_Original.world.stopped();
      }

      Mayflower_Original.world = world;
      if (world != null) {
         Mayflower_Original.world.started();
      }

   }

   public static void start() {
      if (paused) {
         paused = false;
         if (world != null) {
            world.started();
         }

      }
   }

   public static boolean isStopped() {
      return paused;
   }

   public static void stop() {
      if (!paused) {
         paused = true;
         if (world != null) {
            world.stopped();
         }

      }
   }

   public static void setFullScreen(boolean fullscreen) {
   }

   public static void exit() {
      System.exit(0);
   }

   public static void showCursor(boolean showCursor) {
   }

   public static int getDistance2(int x1, int y1, int x2, int y2) {
      int dx = x2 - x1;
      int dy = y2 - y1;
      return dx * dx - dy * dy;
   }

   public static int getDistance(int x1, int y1, int x2, int y2) {
      return (int)Math.sqrt((double)getDistance2(x1, y1, x2, y2));
   }

   public static boolean fullyContains(Shape out, Shape in) {
      if (out != null && in != null) {
         if (in == out) {
            return false;
         } else {
            Area areaOut = new Area(out);
            Area areaIn = new Area(in);
            areaIn.subtract(areaOut);
            return areaIn.isEmpty();
         }
      } else {
         return false;
      }
   }

   public static boolean partiallyContains(Shape a, Shape b) {
      if (a != null && b != null) {
         if (a == b) {
            return false;
         } else if (a.getBounds2D().intersects(b.getBounds2D())) {
            Area areaA = new Area(a);
            Area areaB = new Area(b);
            areaA.intersect(areaB);
            return !areaA.isEmpty();
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   class MouseClick {
      private int x;
      private int y;
      private int clicks;
      private int button;

      public MouseClick(int x, int y, int button, int clicks) {
         this.x = x;
         this.y = y;
         this.button = button;
         this.clicks = clicks;
      }

      public int getX() {
         return this.x;
      }

      public int getY() {
         return this.y;
      }

      public int getNumClicks() {
         return this.clicks;
      }

      public int getButton() {
         return this.button;
      }

      public void addClicks(int num) {
         this.clicks += num;
      }
   }
}
