package mayflower;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D.Double;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import mayflower.util.FastTrig;

public abstract class Actor {
   private MayflowerImage img;
   private Shape bounds = new Rectangle(0, 0, 1, 1);
   private double x;
   private double y;
   private double rotation;
   private World world;
   private AffineTransform at = new AffineTransform();

   public abstract void act();

   public MayflowerImage getImage() {
      return this.img;
   }

   public int getRotation() {
      return this.img == null ? 0 : (int)this.rotation;
   }

   public World getWorld() {
      return this.world;
   }

   public int getX() {
      return (int)this.x;
   }

   public int getY() {
      return (int)this.y;
   }

   public int getCenterX() {
      int cellSize = this.getWorld() == null ? 1 : this.getWorld().getCellSize();
      int x = this.getX() * cellSize;
      if (this.img != null) {
         x += this.img.getWidth() / 2;
      }

      return x;
   }

   public int getCenterY() {
      int cellSize = this.getWorld() == null ? 1 : this.getWorld().getCellSize();
      int y = this.getY() * cellSize;
      if (this.img != null) {
         y += this.img.getHeight() / 2;
      }

      return y;
   }

   public int getWidth() {
      return this.img == null ? 0 : this.img.getWidth();
   }

   public int getHeight() {
      return this.img == null ? 0 : this.img.getHeight();
   }

   public Shape getBounds() {
      return this.at.createTransformedShape(this.bounds);
   }

   public boolean isAtEdge() {
      Shape walls = Mayflower.getBounds();
      return !Mayflower.fullyContains(walls, this.getBounds());
   }

   public void move(int distance) {
      this.move((double)distance);
   }

   public void move(double distance) {
      double rotation = Math.toRadians((double)this.getRotation());
      double dx = distance * FastTrig.cos(rotation);
      double dy = distance * FastTrig.sin(rotation);
      this.x += dx;
      this.y += dy;
      this.at.translate(distance, 0.0D);
   }

   public void setImage(MayflowerImage img) {
      if (img == null) {
         System.out.println("Cannot setImage(null)");
      } else if (this.img == null || !this.img.equals(img)) {
         this.img = img;
         int w = img.getWidth();
         int h = img.getHeight();
         this.bounds = new Rectangle(0, 0, w, h);
      }
   }

   public void setImage(String filename) {
      this.setImage(new MayflowerImage(filename));
   }

   public void setImage(String filename, int x, int y, int w, int h) {
      this.setImage(filename);
      if (this.img == null) {
         System.out.println("Cannot setImage(null)");
      } else {
         this.img.crop(x, y, w, h);
         this.bounds = new Rectangle(0, 0, this.img.getWidth(), this.img.getHeight());
      }
   }

   public void setLocation(double x, double y) {
      if (this.img == null) {
         this.setImage(new MayflowerImage(16, 16, Color.RED));
      }

      this.x = x;
      this.y = y;
      this.at = new AffineTransform();
      this.at.translate(this.x, this.y);
      double hw = (double)(this.img.getWidth() / 2);
      double hh = (double)(this.img.getHeight() / 2);
      this.at.rotate(Math.toRadians(this.rotation), hw, hh);
   }

   public void setRotation(int rotation) {
      if (this.img != null) {
         this.turn(rotation - this.getRotation());
      }

   }

   public void turn(int amount) {
      if (this.img != null) {
         double hw = (double)(this.img.getWidth() / 2);
         double hh = (double)(this.img.getHeight() / 2);
         this.at.rotate(Math.toRadians((double)amount), hw, hh);
         this.rotation += (double)amount;
         this.rotation %= 360.0D;
      }

   }

   public void turnTowards(int x, int y) {
      int dx = x - this.getX();
      int dy = y - this.getY();
      double rad = Math.atan2((double)dy, (double)dx);
      double deg = Math.toDegrees(rad);
      this.setRotation((int)deg);
   }

   public void turnTowards(Actor actor) {
      if (actor != null) {
         MayflowerImage img = actor.getImage();
         if (img == null) {
            this.turnTowards(actor.getX(), actor.getY());
         } else {
            this.turnTowards(actor.getX() + img.getWidth() / 2, actor.getY() + img.getHeight() / 2);
         }

      }
   }

   protected void addedToWorld(World world) {
      this.world = world;
   }

   protected <A extends Actor> List<A> getIntersectingObjects(Class<A> cls) {
      List<A> ret = new LinkedList();
      World w = this.getWorld();
      if (w == null) {
         return ret;
      } else {
         Iterator var5 = w.getObjects(cls).iterator();

         while(var5.hasNext()) {
            A actor = (Actor)var5.next();
            if (this.intersects(actor)) {
               ret.add(actor);
            }
         }

         return ret;
      }
   }

   protected <A extends Actor> List<A> getNeighbors(int distance, boolean diagonal, Class<A> cls) {
      List<A> ret = new LinkedList();
      World w = this.getWorld();
      if (w == null) {
         return ret;
      } else {
         List<Point> nearbyPoints = new LinkedList();
         List<Point> offsets = new LinkedList();
         offsets.add(new Point(1, 0));
         offsets.add(new Point(-1, 0));
         offsets.add(new Point(0, 1));
         offsets.add(new Point(0, -1));
         if (diagonal) {
            offsets.add(new Point(1, 1));
            offsets.add(new Point(-1, -1));
            offsets.add(new Point(-1, 1));
            offsets.add(new Point(1, -1));
         }

         List<Point> lastRing = new LinkedList();
         lastRing.add(new Point(this.getCenterX(), this.getCenterY()));

         for(int i = 0; i < distance; ++i) {
            List<Point> newPoints = new LinkedList();
            Iterator var12 = lastRing.iterator();

            while(var12.hasNext()) {
               Point point = (Point)var12.next();
               Iterator var14 = offsets.iterator();

               while(var14.hasNext()) {
                  Point offset = (Point)var14.next();
                  int x = (int)(point.getX() + offset.getX() * (double)w.getCellSize());
                  int y = (int)(point.getY() + offset.getY() * (double)w.getCellSize());
                  Point neighborPoint = new Point(x, y);
                  if (!nearbyPoints.contains(neighborPoint)) {
                     nearbyPoints.add(neighborPoint);
                     newPoints.add(neighborPoint);
                     List<A> neighbors = w.getObjectsAt((int)(neighborPoint.getX() / (double)w.getCellSize()), (int)(neighborPoint.getY() / (double)w.getCellSize()), cls);
                     ret.addAll(neighbors);
                  }
               }
            }

            lastRing = newPoints;
         }

         return ret;
      }
   }

   protected <A extends Actor> List<A> getObjectsAtOffset(int dx, int dy, Class<A> cls) {
      List<A> ret = new LinkedList();
      World w = this.getWorld();
      if (w == null) {
         return ret;
      } else {
         int cellSize = w.getCellSize();
         int x = dx * cellSize + this.getCenterX();
         int y = dy * cellSize + this.getCenterY();
         Iterator var10 = w.getObjects(cls).iterator();

         while(var10.hasNext()) {
            A actor = (Actor)var10.next();
            if (actor != this && actor.getBounds().contains((double)x, (double)y)) {
               ret.add(actor);
            }
         }

         return ret;
      }
   }

   protected <A extends Actor> List<A> getObjectsInRange(int radius, Class<A> cls) {
      int x = this.getCenterX();
      int y = this.getCenterY();
      List<A> ret = new LinkedList();
      World w = this.getWorld();
      if (w == null) {
         return ret;
      } else {
         int diameter = radius * 2;
         Shape range = new Double((double)(x - radius), (double)(y - radius), (double)diameter, (double)diameter);
         Iterator var10 = w.getObjects(cls).iterator();

         while(var10.hasNext()) {
            A actor = (Actor)var10.next();
            Shape bounds = actor.getBounds();
            if (actor != this && Mayflower.partiallyContains(range, bounds)) {
               ret.add(actor);
            }
         }

         return ret;
      }
   }

   protected <A extends Actor> A getOneIntersectingObject(Class<A> cls) {
      List<A> ret = this.getIntersectingObjects(cls);
      return ret.size() == 0 ? null : (Actor)ret.get(0);
   }

   protected <A extends Actor> A getOneObjectAtOffset(int dx, int dy, Class<A> cls) {
      List<A> ret = this.getObjectsAtOffset(dx, dy, cls);
      return ret.size() == 0 ? null : (Actor)ret.get(0);
   }

   protected boolean intersects(Actor other) {
      if (other != null && other != this) {
         Shape oBounds = other.getBounds();
         return Mayflower.partiallyContains(this.getBounds(), oBounds);
      } else {
         return false;
      }
   }

   protected boolean isTouching(Class<? extends Actor> cls) {
      return this.getIntersectingObjects(cls).size() > 0;
   }

   protected boolean isTouchingAtOffset(int dx, int dy, Class<? extends Actor> cls) {
      return this.getOneObjectAtOffset(dx, dy, cls) != null;
   }

   protected boolean isTouchingPerfect(Actor other) {
      if (this.getWorld() != null && other != null && this.getWorld() == other.getWorld()) {
         BufferedImage perfectBufferA = new BufferedImage(this.getWorld().getWidth(), this.getWorld().getHeight(), 2);
         BufferedImage perfectBufferB = new BufferedImage(this.getWorld().getWidth(), this.getWorld().getHeight(), 2);
         Graphics2D graphicsA = (Graphics2D)perfectBufferA.getGraphics();
         Graphics2D graphicsB = (Graphics2D)perfectBufferB.getGraphics();
         this.draw(graphicsA);
         other.draw(graphicsB);
         Rectangle shapeA = this.getBounds().getBounds();
         if (this.getImage().getWidth() * this.getImage().getHeight() > other.getImage().getWidth() * other.getImage().getHeight()) {
            shapeA = other.getBounds().getBounds();
         }

         for(int x = (int)shapeA.getMinX(); x < (int)shapeA.getMaxX() && x < perfectBufferA.getWidth(); ++x) {
            for(int y = (int)shapeA.getMinY(); y < (int)shapeA.getMaxY() && y < perfectBufferB.getHeight(); ++y) {
               int transparentA = (perfectBufferA.getRGB(x, y) & -16777216) >> 24;
               int transparentB = (perfectBufferB.getRGB(x, y) & -16777216) >> 24;
               if (transparentA != 0 && transparentB != 0) {
                  return true;
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   protected <A extends Actor> void removeTouching(Class<A> cls) {
      this.world.removeObjects(this.getIntersectingObjects(cls));
   }

   public void scale(int width, int height) {
      if (this.img != null) {
         float dw = (float)width / (float)this.img.getWidth();
         float dh = (float)height / (float)this.img.getHeight();
         this.at.scale((double)dw, (double)dh);
         float dx = (float)this.getCenterX() - dw / 2.0F;
         float dy = (float)this.getCenterY() - dh / 2.0F;
         this.at.translate((double)dx, (double)dy);
      }
   }

   public void scale(double scale) {
      if (this.img != null) {
         this.scale((int)((double)this.img.getWidth() * scale), (int)((double)this.img.getHeight() * scale));
      }
   }

   public void draw(Graphics2D g) {
      if (this.img != null && this.img.getBufferedImage() != null) {
         g.drawImage(this.img.getBufferedImage(), this.at, (ImageObserver)null);
      }
   }

   public boolean contains(int x, int y) {
      return this.getBounds().contains((double)x, (double)y);
   }
}
