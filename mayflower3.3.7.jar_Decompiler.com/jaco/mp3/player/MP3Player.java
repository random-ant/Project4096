package jaco.mp3.player;

import jaco.mp3.player.plaf.MP3PlayerUI;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class MP3Player extends JPanel {
   public static final String UI_CLASS_ID = MP3Player.class.getName() + "UI";
   private final Random a = new Random();
   private List b;
   private List c = new Vector();
   private int d;
   private boolean e = false;
   private boolean f = false;
   private volatile boolean g = false;
   private volatile boolean h = true;
   private volatile boolean i = false;

   static {
      if (UIManager.getDefaults().get(UI_CLASS_ID) == null) {
         setDefaultUI(MP3PlayerUI.class);
      }

   }

   public String getUIClassID() {
      return UI_CLASS_ID;
   }

   public static void setDefaultUI(Class var0) {
      UIManager.getDefaults().put(UI_CLASS_ID, var0.getName());
   }

   public MP3Player() {
   }

   public MP3Player(URL... var1) {
      URL[] var4 = var1;
      int var3 = var1.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         URL var5 = var4[var2];
         this.addToPlayList(var5);
      }

   }

   public MP3Player(File... var1) {
      File[] var4 = var1;
      int var3 = var1.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         File var5 = var4[var2];
         this.addToPlayList(var5);
      }

   }

   public synchronized void play() {
      if (this.b != null) {
         Iterator var2 = this.b.iterator();

         while(var2.hasNext()) {
            ((N)var2.next()).a();
         }
      }

      if (this.g) {
         this.g = false;
      } else {
         this.a();
         this.h = false;
         C var1;
         (var1 = new C(this)).setDaemon(true);
         var1.start();
      }
   }

   public synchronized void pause() {
      if (this.b != null) {
         Iterator var2 = this.b.iterator();

         while(var2.hasNext()) {
            ((N)var2.next()).b();
         }
      }

      this.g = true;
   }

   public synchronized void stop() {
      if (this.b != null) {
         Iterator var1 = this.b.iterator();

         while(var1.hasNext()) {
            var1.next();
         }
      }

      this.a();
   }

   private void a() {
      this.g = false;
      this.i = true;

      while(this.i && !this.h) {
         try {
            Thread.sleep(10L);
         } catch (Exception var1) {
         }
      }

      this.i = false;
      this.h = true;
   }

   public synchronized void skipForward() {
      if (this.e) {
         this.d = this.a.nextInt(this.c.size());
         this.play();
      } else {
         if (this.d >= this.c.size() - 1) {
            if (this.f) {
               this.d = 0;
               this.play();
               return;
            }
         } else {
            ++this.d;
            this.play();
         }

      }
   }

   public synchronized void skipBackward() {
      if (this.e) {
         this.d = this.a.nextInt(this.c.size());
         this.play();
      } else {
         if (this.d <= 0) {
            if (this.f) {
               this.d = this.c.size() - 1;
               this.play();
               return;
            }
         } else {
            --this.d;
            this.play();
         }

      }
   }

   public boolean isPaused() {
      return this.g;
   }

   public boolean isStopped() {
      return this.h;
   }

   public synchronized void addMP3PlayerListener(N var1) {
      if (this.b == null) {
         this.b = new ArrayList();
      }

      this.b.add(var1);
   }

   public synchronized void removeMP3PlayerListener(N var1) {
      this.b.remove(var1);
   }

   public synchronized void removeAllMP3PlayerListeners() {
      this.b.clear();
   }

   public void addToPlayList(URL var1) {
      this.c.add(var1);
   }

   public void addToPlayList(File var1) {
      try {
         this.c.add(var1.toURI().toURL());
      } catch (Exception var2) {
         throw new RuntimeException(var2);
      }
   }

   public List getPlayList() {
      return this.c;
   }

   public boolean isShuffle() {
      return this.e;
   }

   public void setShuffle(boolean var1) {
      this.e = var1;
   }

   public boolean isRepeat() {
      return this.f;
   }

   public void setRepeat(boolean var1) {
      this.f = var1;
   }

   // $FF: synthetic method
   static List a(MP3Player var0) {
      return var0.c;
   }

   // $FF: synthetic method
   static int b(MP3Player var0) {
      return var0.d;
   }

   // $FF: synthetic method
   static boolean c(MP3Player var0) {
      return var0.i;
   }

   // $FF: synthetic method
   static boolean d(MP3Player var0) {
      return var0.g;
   }

   // $FF: synthetic method
   static void a(MP3Player var0, boolean var1) {
      var0.i = false;
   }

   // $FF: synthetic method
   static void b(MP3Player var0, boolean var1) {
      var0.h = true;
   }
}
