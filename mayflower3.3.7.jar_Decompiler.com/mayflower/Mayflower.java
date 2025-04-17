package mayflower;

import jaco.mp3.player.MP3Player;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
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

public abstract class Mayflower {
   public static final String version = "3.3.1";
   private World world;
   private Set<Integer> pressedKeys;
   private Set<Integer> wasPressedKeys;
   private int lastKeyPressed;
   private int width;
   private int height;
   private boolean showBounds;
   private Shape bounds;
   private Map<String, Sound> sounds;
   private Map<String, MP3Player> musics;
   private boolean fullscreen;
   private int mouseX;
   private int mouseY;
   private int mouseButton;
   private boolean mouseMoved;
   private boolean mousePressed;
   private boolean lastMousePressed;
   private boolean nowMousePressed;
   private Map<String, Mayflower.MouseClick> clicks;
   private Map<String, Mayflower.MouseClick> pastClicks;
   private boolean paused;
   private JFrame frame;
   private MayflowerPanel panel;
   private MayflowerKeyboardListener keyboardListener;
   private MayflowerMouseListener mouseListener;
   private MayflowerThread thread;
   private BufferedImage buffer;
   private BufferedImage textLayer;
   private Set<Object> oldHoveredObjects;
   private Set<Object> hoveredObjects;
   private int mouseOffsetX;
   private int mouseOffsetY;
   private long lastTick;
   private long tickWait;
   private static Mayflower instance;

   public Mayflower(String title, int width, int height) {
      this(title, width, height, true);
   }

   public Mayflower(String title, int width, int height, boolean syncfps) {
      this.mouseButton = -1;
      this.lastTick = 0L;
      this.tickWait = 16393442L;
      this.width = width;
      this.height = height;
      this.paused = false;
      this.pressedKeys = new HashSet();
      this.wasPressedKeys = new HashSet();
      this.sounds = new HashMap();
      this.musics = new HashMap();
      this.clicks = new HashMap();
      this.pastClicks = new HashMap();
      this.hoveredObjects = new HashSet();
      this.oldHoveredObjects = new HashSet();
      this.bounds = new Rectangle(-1, -1, width + 3, height + 3);
      if (instance == null) {
         instance = this;
      }

      this.initGUI(title);
      this.thread = new MayflowerThread(this);
      this.thread.start();
   }

   public static void main(String[] args) {
      String[] fonts = Font.getAvailableFonts();
      String[] var5 = fonts;
      int var4 = fonts.length;

      for(int var3 = 0; var3 < var4; ++var3) {
         String font = var5[var3];
         System.out.println(font);
      }

   }

   public void initGUI(String title) {
      TinySound.init();
      this.frame = new JFrame(title);
      this.panel = new MayflowerPanel(this);
      this.frame.getContentPane().add(this.panel, "Center");
      this.keyboardListener = new MayflowerKeyboardListener(this);
      this.frame.addKeyListener(this.keyboardListener);
      this.mouseListener = new MayflowerMouseListener(this);
      this.frame.addMouseListener(this.mouseListener);
      this.frame.addMouseMotionListener(this.mouseListener);
      this.init();
      JFrame temp = new JFrame();
      temp.pack();
      Insets insets = temp.getInsets();
      temp = null;
      this.frame.setDefaultCloseOperation(3);
      this.frame.setSize(insets.left + this.width - 2, insets.top + this.height - 2);
      this.frame.setResizable(false);
      this.frame.setVisible(true);
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
      if (this.world != null) {
         this.textLayer = new BufferedImage(this.width, this.height, 2);
         BufferedImage buffer = new BufferedImage(this.width, this.height, 2);
         Graphics2D g = buffer.createGraphics();
         MayflowerImage background = this.world.getBackground();
         if (background != null) {
            g.drawImage(background.getBufferedImage(), 0, 0, (ImageObserver)null);
         }

         Set<Actor> painted = new HashSet();
         Iterator var6 = this.world.getPaintOrder().iterator();

         int var9;
         int var10;
         while(var6.hasNext()) {
            Class<? extends Actor> drawClass = (Class)var6.next();
            Actor[] actors = (Actor[])this.world.getObjects(drawClass).toArray(new Actor[0]);
            Actor[] var11 = actors;
            var10 = actors.length;

            for(var9 = 0; var9 < var10; ++var9) {
               Actor actor = var11[var9];
               if (!painted.contains(actor)) {
                  painted.add(actor);
                  actor.draw(g);
                  if (this.showBounds) {
                     Shape s = actor.getBounds();
                     g.setColor(java.awt.Color.BLACK);
                     g.draw(s);
                  }
               }
            }
         }

         this.world.render(g);
         Graphics2D g2d = (Graphics2D)this.textLayer.getGraphics();
         Map<String, World.TextInfo> texts = this.world.getTexts();
         String[] keys = (String[])texts.keySet().toArray(new String[0]);
         String[] var17 = keys;
         var10 = keys.length;

         for(var9 = 0; var9 < var10; ++var9) {
            String key = var17[var9];
            World.TextInfo info = (World.TextInfo)texts.get(key);
            g2d.setColor(info.getColor().getAwtColor());
            g2d.setFont(info.getFont().getAwtFont());
            g2d.drawString(info.getText(), info.getX(), info.getY());
         }

         g.drawImage(this.textLayer, 0, 0, (ImageObserver)null);
         this.buffer = buffer;
         this.panel.repaint();
      }
   }

   public BufferedImage getBuffer() {
      return this.buffer;
   }

   public void update() {
      if (this.world != null) {
         if (this.fullscreen && this._isKeyDown(27)) {
            this._setFullScreen(false);
         }

         this.world.act();
         if (!this.paused) {
            Set<Actor> acted = new HashSet();
            Iterator var3 = this.world.getActOrder().iterator();

            while(var3.hasNext()) {
               Class<? extends Actor> actClass = (Class)var3.next();
               Iterator var5 = this.world.getObjects(actClass).iterator();

               while(var5.hasNext()) {
                  Actor actor = (Actor)var5.next();
                  if (actor.getWorld() != null && !acted.contains(actor)) {
                     acted.add(actor);
                     actor.act();
                  }
               }
            }
         }

         this.wasPressedKeys.clear();
         this.wasPressedKeys.addAll(this.pressedKeys);
         this.pastClicks.clear();
         Iterator var8 = this.clicks.keySet().iterator();

         while(var8.hasNext()) {
            String key = (String)var8.next();
            this.pastClicks.put(key, (Mayflower.MouseClick)this.clicks.get(key));
         }

         this.oldHoveredObjects.clear();
         this.oldHoveredObjects.addAll(this.hoveredObjects);
         this.hoveredObjects.clear();
         MouseInfo mi = getMouseInfo();
         List<Actor> hovered = this.world.getObjectsAt(mi.getX(), mi.getY());
         this.hoveredObjects.addAll(hovered);
         this.clicks.clear();
         this.mouseMoved = false;
         this.lastMousePressed = this.mousePressed;
         this.mousePressed = false;
      }
   }

   public void keyPressed(int key, char c) {
      this.pressedKeys.add(key);
      this.lastKeyPressed = key;
   }

   public void keyReleased(int key, char c) {
      this.pressedKeys.remove(key);
   }

   public void mousePressed(int button, int x, int y) {
      this.mousePressed = true;
      this.mouseButton = button;
      this.nowMousePressed = true;
      this.mouseX = x;
      this.mouseY = y;
   }

   public void mouseReleased(int button, int x, int y) {
      this.mousePressed = false;
      this.nowMousePressed = false;
      this.mouseButton = -1;
   }

   public void mouseDragged(int oldX, int oldy, int newx, int newy) {
      this.mouseX = newx;
      this.mouseY = newy;
   }

   public void mouseClicked(int button, int x, int y, int clickCount) {
      String key = x + " " + y + " " + button;
      Mayflower.MouseClick info = (Mayflower.MouseClick)this.clicks.get(key);
      if (info == null) {
         this.clicks.put(key, new Mayflower.MouseClick(x, y, button, clickCount));
      } else {
         info.addClicks(clickCount);
      }

   }

   public void mouseMoved(int oldx, int oldy, int newx, int newy) {
      this.mouseX = newx;
      this.mouseY = newy;
      this.mouseMoved = true;
   }

   public Shape _getBounds() {
      return this.bounds;
   }

   public void _showBounds(boolean showBounds) {
      this.showBounds = showBounds;
   }

   public void _showFPS(boolean showFPS) {
   }

   public int _getWidth() {
      return this.width;
   }

   public int _getHeight() {
      return this.height;
   }

   public void _delay(int time) {
      Timer t = new Timer(time);

      while(!t.isDone()) {
         Thread.yield();
      }

   }

   public int _getKey() {
      return this.lastKeyPressed;
   }

   public MouseInfo _getMouseInfo() {
      Actor act = null;
      if (this.world != null) {
         List<Actor> acts = this.world.getObjectsAt(this.mouseX + this.mouseOffsetX, this.mouseY + this.mouseOffsetY);
         if (acts.size() > 0) {
            act = (Actor)acts.get(0);
         }
      }

      return new MouseInfo(act, this.mouseButton, this.mouseButton >= 0 ? 1 : 0, this.mouseX + this.mouseOffsetX, this.mouseY + this.mouseOffsetY);
   }

   public boolean _isKeyDown(int keyName) {
      return this.pressedKeys.contains(keyName);
   }

   public boolean _wasKeyDown(int keyName) {
      return this.wasPressedKeys.contains(keyName);
   }

   public boolean _isKeyPressed(int keyName) {
      return this._isKeyDown(keyName) && !this._wasKeyDown(keyName);
   }

   public List<Actor> _mouseClicked() {
      MouseInfo mi = getMouseInfo();
      return this.world.getObjectsAt(mi.getX(), mi.getY());
   }

   public boolean _mouseClicked(Object obj) {
      Collection<Mayflower.MouseClick> clicks = this.clicks.values();
      if (obj == null) {
         return clicks.size() > 0;
      } else {
         Mayflower.MouseClick click;
         Iterator var5;
         if (obj instanceof World) {
            World world = (World)obj;
            var5 = clicks.iterator();

            while(var5.hasNext()) {
               click = (Mayflower.MouseClick)var5.next();
               if (world.getObjectsAt(click.getX(), click.getY()).size() > 0) {
                  return false;
               }
            }

            return true;
         } else {
            if (obj instanceof Actor) {
               Actor actor = (Actor)obj;
               var5 = clicks.iterator();

               while(var5.hasNext()) {
                  click = (Mayflower.MouseClick)var5.next();
                  if (actor.getBounds().contains((double)click.getX(), (double)click.getY())) {
                     return true;
                  }
               }
            }

            return false;
         }
      }
   }

   public boolean _wasMouseClicked(Object obj) {
      Collection<Mayflower.MouseClick> clicks = this.pastClicks.values();
      if (obj == null) {
         return clicks.size() > 0;
      } else {
         Mayflower.MouseClick click;
         Iterator var5;
         if (obj instanceof World) {
            World world = (World)obj;
            var5 = clicks.iterator();

            while(var5.hasNext()) {
               click = (Mayflower.MouseClick)var5.next();
               if (world.getObjectsAt(click.getX(), click.getY()).size() > 0) {
                  return false;
               }
            }

            return true;
         } else {
            if (obj instanceof Actor) {
               Actor actor = (Actor)obj;
               var5 = clicks.iterator();

               while(var5.hasNext()) {
                  click = (Mayflower.MouseClick)var5.next();
                  if (actor.getBounds().contains((double)click.getX(), (double)click.getY())) {
                     return true;
                  }
               }
            }

            return false;
         }
      }
   }

   public boolean _mouseHovered(Object obj) {
      return obj == null ? false : this.hoveredObjects.contains(obj);
   }

   private boolean _wasMouseHovered(Object obj) {
      return obj == null ? false : this.oldHoveredObjects.contains(obj);
   }

   public boolean _mouseDragEnded(Object obj) {
      System.out.println("mouseDragEnded is not implemented yet");
      return false;
   }

   public boolean _mouseDragged(Object obj) {
      System.out.println("mouseDragged is not implemented yet");
      return false;
   }

   public boolean _mouseDown(Object obj) {
      if (this.nowMousePressed) {
         if (obj == null) {
            return true;
         }

         if (obj instanceof World) {
            if (((World)obj).getObjectsAt(this.mouseX + this.mouseOffsetX, this.mouseY + this.mouseOffsetY).size() == 0) {
               return true;
            }

            return false;
         }

         if (obj instanceof Actor) {
            return ((Actor)obj).getBounds().contains((double)(this.mouseX + this.mouseOffsetX), (double)(this.mouseY + this.mouseOffsetY));
         }
      }

      return false;
   }

   public boolean _mousePressed(Object obj) {
      if (this.mousePressed && !this.lastMousePressed) {
         if (obj == null) {
            return true;
         }

         if (obj instanceof World) {
            if (((World)obj).getObjectsAt(this.mouseX + this.mouseOffsetX, this.mouseY + this.mouseOffsetY).size() == 0) {
               return true;
            }

            return false;
         }

         if (obj instanceof Actor) {
            return ((Actor)obj).getBounds().contains((double)(this.mouseX + this.mouseOffsetX), (double)(this.mouseY + this.mouseOffsetY));
         }
      }

      return false;
   }

   public boolean _mouseMoved(Object obj) {
      if (!this.mouseMoved) {
         return false;
      } else if (obj == null) {
         return true;
      } else if (obj instanceof World) {
         return ((World)obj).getObjectsAt(this.mouseX + this.mouseOffsetX, this.mouseY + this.mouseOffsetY).size() == 0;
      } else {
         return obj instanceof Actor ? ((Actor)obj).getBounds().contains((double)(this.mouseX + this.mouseOffsetX), (double)(this.mouseY + this.mouseOffsetY)) : false;
      }
   }

   public void _loadSound(String soundFile) {
      File file = new File(soundFile);
      if (file.exists()) {
         Sound sound = TinySound.loadSound(file);
         this.sounds.put(soundFile, sound);
      }

   }

   public void _loadMusic(String musicFile) {
      File file = new File(musicFile);
      if (file.exists()) {
         MP3Player player = new MP3Player(new File[]{file});
         this.musics.put(musicFile, player);
      }

   }

   public void _playSound(String soundFile) {
      Sound sound = (Sound)this.sounds.get(soundFile);
      if (sound == null) {
         this._loadSound(soundFile);
         sound = (Sound)this.sounds.get(soundFile);
      }

      if (sound != null) {
         sound.play();
      }

   }

   public void _stopMusic(String musicFile) {
      MP3Player music = (MP3Player)this.musics.get(musicFile);
      if (music != null) {
         music.stop();
      }

   }

   public void _playMusic(String musicFile) {
      MP3Player music = (MP3Player)this.musics.get(musicFile);
      if (music == null) {
         this._loadMusic(musicFile);
         music = (MP3Player)this.musics.get(musicFile);
      }

      if (music != null) {
         try {
            music.play();
         } catch (Exception var4) {
         }
      }

   }

   public void _setSpeed(int speed) {
      System.out.println("Setting speed doesn't do anything...");
   }

   public void _setWorld(World world) {
      if (this.world != null) {
         this.world.stopped();
      }

      this.world = world;
      if (world != null) {
         this.world.started();
      }

   }

   public void _start() {
      if (this.paused) {
         this.paused = false;
         if (this.world != null) {
            this.world.started();
         }

      }
   }

   public boolean _isStopped() {
      return this.paused;
   }

   public void _stop() {
      if (!this.paused) {
         this.paused = true;
         if (this.world != null) {
            this.world.stopped();
         }

      }
   }

   public void _setFullScreen(boolean fullscreen) {
   }

   public void _showCursor(boolean showCursor) {
   }

   public void _setMouseOffset(int x, int y) {
      this.mouseOffsetX = x;
      this.mouseOffsetY = y;
   }

   public static void setMouseOffset(int x, int y) {
      instance._setMouseOffset(x, y);
   }

   public static Shape getBounds() {
      return instance._getBounds();
   }

   public static void showBounds(boolean showBounds) {
      instance._showBounds(showBounds);
   }

   public static void showFPS(boolean showFPS) {
      instance._showFPS(showFPS);
   }

   public static int getWidth() {
      return instance._getWidth();
   }

   public static int getHeight() {
      return instance._getHeight();
   }

   public static String ask(String prompt) {
      return JOptionPane.showInputDialog(prompt);
   }

   public static void delay(int time) {
      instance._delay(time);
   }

   public static int getKey() {
      return instance._getKey();
   }

   public static MouseInfo getMouseInfo() {
      return instance._getMouseInfo();
   }

   public static boolean isKeyDown(int keyName) {
      return instance._isKeyDown(keyName);
   }

   public static boolean wasKeyDown(int keyName) {
      return instance._wasKeyDown(keyName);
   }

   public static boolean isKeyPressed(int keyName) {
      return instance._isKeyPressed(keyName);
   }

   public static boolean mouseClicked(Object obj) {
      return instance._mouseClicked(obj);
   }

   public static List<Actor> mouseClicked() {
      return instance._mouseClicked();
   }

   public static boolean wasMouseClicked(Object obj) {
      return instance._wasMouseClicked(obj);
   }

   public static boolean mouseHovered(Object obj) {
      return instance._mouseHovered(obj);
   }

   public static boolean wasMouseHovered(Object obj) {
      return instance._wasMouseHovered(obj);
   }

   public static boolean mouseDragEnded(Object obj) {
      return instance._mouseDragEnded(obj);
   }

   public static boolean mouseDragged(Object obj) {
      return instance._mouseDragged(obj);
   }

   public static boolean mouseDown(Object obj) {
      return instance._mouseDown(obj);
   }

   public static boolean mousePressed(Object obj) {
      return instance._mousePressed(obj);
   }

   public static boolean mouseMoved(Object obj) {
      return instance._mouseMoved(obj);
   }

   public static void loadSound(String soundFile) {
      instance._loadSound(soundFile);
   }

   public static void loadMusic(String musicFile) {
      instance._loadMusic(musicFile);
   }

   public static void playSound(String soundFile) {
      instance._playSound(soundFile);
   }

   public static void stopMusic(String musicFile) {
      instance._stopMusic(musicFile);
   }

   public static void playMusic(String musicFile) {
      instance._playMusic(musicFile);
   }

   public static void setSpeed(int speed) {
      instance._setSpeed(speed);
   }

   public static void setWorld(World world) {
      instance._setWorld(world);
   }

   public static void start() {
      instance._start();
   }

   public static boolean isStopped() {
      return instance._isStopped();
   }

   public static void stop() {
      instance._stop();
   }

   public static void setFullScreen(boolean fullscreen) {
      instance._setFullScreen(fullscreen);
   }

   public static void showCursor(boolean showCursor) {
      instance._showCursor(showCursor);
   }

   public static void exit() {
      System.exit(0);
   }

   public static long getTime() {
      return System.nanoTime();
   }

   public static int getRandomNumber(int limit) {
      return (int)(Math.random() * (double)limit);
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

   public static boolean pixelPerfectTouching(Actor a, Actor b) {
      System.out.println("pixelPerfectTouching is not implemented yet");
      if (a != null && b != null) {
         if (partiallyContains(a.getBounds(), b.getBounds())) {
            boolean[][] maskA = a.getImage().getMask();
            boolean[][] maskB = b.getImage().getMask();
            int aOffX = a.getX();
            int aOffY = a.getY();
            int bOffX = b.getX();
            int bOffY = b.getY();
            Set<Point> points = new HashSet();

            int r;
            int c;
            int x;
            int y;
            for(r = 0; r < maskA.length; ++r) {
               for(c = 0; c < maskA[r].length; ++c) {
                  if (maskA[r][c]) {
                     x = aOffX + c;
                     y = aOffY + r;
                     points.add(new Point(x, y));
                  }
               }
            }

            for(r = 0; r < maskB.length; ++r) {
               for(c = 0; c < maskB[r].length; ++c) {
                  x = bOffX + c;
                  y = bOffY + r;
                  if (maskB[r][c] && !points.add(new Point(x, y))) {
                     return true;
                  }
               }
            }
         }

         return false;
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
