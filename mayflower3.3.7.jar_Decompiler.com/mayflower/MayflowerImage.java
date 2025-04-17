package mayflower;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MayflowerImage {
   private BufferedImage img;
   private double rotation;
   private int width;
   private int height;
   private int transparency;
   private BufferedImage original;
   private boolean[][] mask;

   private void init() {
      this.width = this.img.getWidth();
      this.height = this.img.getHeight();
      BufferedImage copy = new BufferedImage(this.img.getWidth(), this.img.getHeight(), 2);
      copy.getGraphics().drawImage(this.img, 0, 0, (ImageObserver)null);
      this.original = copy;
   }

   private BufferedImage getOriginal() {
      BufferedImage copy = new BufferedImage(this.original.getWidth(), this.original.getHeight(), 2);
      copy.getGraphics().drawImage(this.original, 0, 0, (ImageObserver)null);
      return copy;
   }

   public MayflowerImage(MayflowerImage img) {
      if (img == null) {
         System.out.println("Do not instantiate a MayflowerImage with NULL");
      }

      BufferedImage copy = new BufferedImage(img.getWidth(), img.getHeight(), 2);
      copy.getGraphics().drawImage(img.getBufferedImage(), 0, 0, (ImageObserver)null);
      this.img = copy;
      this.init();
   }

   public MayflowerImage(int width, int height) {
      this.img = new BufferedImage(width, height, 2);
      this.init();
   }

   public MayflowerImage(String filename) {
      try {
         this.img = ImageIO.read(new File(filename));
         this.init();
      } catch (IOException var3) {
         System.out.println("Cannnot read image: " + filename);
         var3.printStackTrace();
      }

   }

   public MayflowerImage(int width, int height, Color color) {
      this.img = new BufferedImage(width, height, 2);
      Graphics graphics = this.img.getGraphics();
      graphics.setColor(color.getAwtColor());
      graphics.fillRect(0, 0, this.img.getWidth(), this.img.getHeight());
      this.init();
   }

   public MayflowerImage(String string, int size, Color foreground) {
      this(string, size, foreground, (Color)null, (Color)null);
   }

   public MayflowerImage(String string, int size, Color foreground, Color notused, Color notused2) {
   }

   public void crop(int x, int y, int w, int h) {
      this.width = w - x;
      this.height = h - y;
      this.img = this.img.getSubimage(x, y, w, h);
   }

   public boolean[][] getMask() {
      if (this.img == null) {
         return new boolean[0][0];
      } else {
         if (this.mask == null) {
            this.mask = new boolean[this.img.getHeight()][this.img.getWidth()];

            for(int r = 0; r < this.img.getHeight(); ++r) {
               for(int c = 0; c < this.img.getWidth(); ++c) {
                  int pixel = this.img.getRGB(c, r);
                  boolean transparent = pixel >> 24 == 0;
                  this.mask[r][c] = !transparent;
               }
            }
         }

         return this.mask;
      }
   }

   public BufferedImage getBufferedImage() {
      return this.img;
   }

   public Color getColorAt(int x, int y) {
      if (this.img == null) {
         return null;
      } else {
         int clr = this.img.getRGB(x, y);
         int red = (clr & 16711680) >> 16;
         int green = (clr & '\uff00') >> 8;
         int blue = clr & 255;
         return new Color(red, green, blue);
      }
   }

   public void setColorAt(Color c, int x, int y) {
      if (this.img != null) {
         if (c != null) {
            if (x >= 0 && y >= 0 && x < this.width && y < this.height) {
               this.img.setRGB(x, y, c.getAwtColor().getRGB());
            }
         }
      }
   }

   public int getHeight() {
      return this.img == null ? 0 : this.img.getHeight();
   }

   public int getTransparency() {
      return this.transparency;
   }

   public int getWidth() {
      return this.img == null ? 0 : this.img.getWidth();
   }

   private BufferedImage createTransformed(BufferedImage image, AffineTransform at) {
      BufferedImage newImage = new BufferedImage(this.width, this.height, 2);
      Graphics2D g = newImage.createGraphics();
      g.transform(at);
      g.drawImage(image, 0, 0, (ImageObserver)null);
      g.dispose();
      return newImage;
   }

   public void mirrorHorizontally() {
      if (this.img != null) {
         AffineTransform at = new AffineTransform();
         at.concatenate(AffineTransform.getScaleInstance(-1.0D, 1.0D));
         at.concatenate(AffineTransform.getTranslateInstance((double)(-this.img.getWidth()), 0.0D));
         this.img = this.createTransformed(this.img, at);
         this.original = this.createTransformed(this.original, at);
         this.mask = null;
      }
   }

   public void mirrorVertically() {
      if (this.img != null) {
         AffineTransform at = new AffineTransform();
         at.concatenate(AffineTransform.getScaleInstance(1.0D, -1.0D));
         at.concatenate(AffineTransform.getTranslateInstance(0.0D, (double)(-this.img.getHeight())));
         this.img = this.createTransformed(this.img, at);
         this.original = this.createTransformed(this.original, at);
         this.mask = null;
      }
   }

   public void rotate(double degrees) {
      if (this.img != null) {
         this.rotation += degrees;
         this.rotation %= 360.0D;
         double rads = Math.toRadians(this.rotation);
         AffineTransform at = AffineTransform.getRotateInstance(rads, (double)this.img.getWidth() / 2.0D, (double)this.img.getHeight() / 2.0D);
         this.img = this.createTransformed(this.getOriginal(), at);
         this.mask = null;
      }
   }

   public void setRotation(double degrees) {
      this.rotate(degrees - this.rotation);
   }

   public double getRotation() {
      return this.img == null ? 0.0D : this.rotation;
   }

   public void scale(int width, int height) {
      if (this.img != null) {
         double offX = (double)width / (double)this.getWidth();
         double offY = (double)height / (double)this.getHeight();
         this.width = width;
         this.height = height;
         AffineTransform at = AffineTransform.getScaleInstance(offX, offY);
         this.img = this.createTransformed(this.img, at);
         this.original = this.createTransformed(this.original, at);
         this.mask = null;
      }
   }

   public void scale(double scale) {
      if (this.img != null) {
         this.scale((int)((double)this.width * scale), (int)((double)this.height * scale));
      }
   }

   public void setColorAt(int x, int y, Color color) {
      if (this.img != null) {
         if (color != null) {
            this.img.setRGB(x, y, color.getAwtColor().getRGB());
            this.mask = null;
         }
      }
   }

   public void setTransparency(int t) {
      if (this.img != null) {
         this.transparency = t;
         if (t == 100) {
            t = 99;
         }

         t = 100 - t;
         int alpha = (int)((double)t / 100.0D * 255.0D);
         alpha <<= 24;

         for(int w = 0; w < this.width; ++w) {
            for(int h = 0; h < this.height; ++h) {
               int clr = this.img.getRGB(w, h);
               int rgb = clr & 16777215;
               int a = (clr & -16777216) >> 24;
               if (a != 0) {
                  int rgba = rgb | alpha;
                  this.img.setRGB(w, h, rgba);
               }
            }
         }

      }
   }

   public String toString() {
      return this.img.toString();
   }
}
