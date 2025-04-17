package jaco.mp3.player.plaf;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;

final class c implements LayoutManager {
   // $FF: synthetic field
   private MP3PlayerUICompact a;

   c(MP3PlayerUICompact var1) {
      this.a = var1;
      super();
   }

   public final void layoutContainer(Container var1) {
      synchronized(var1.getTreeLock()) {
         Dimension var6 = var1.getSize();
         Dimension var3 = MP3PlayerUICompact.a(this.a).getPreferredSize();
         Rectangle var4;
         (var4 = new Rectangle(var3)).x = (var6.width - var3.width) / 2;
         var4.y = (var6.height - var3.height) / 2;
         MP3PlayerUICompact.a(this.a).setBounds(var4);
      }
   }

   public final Dimension preferredLayoutSize(Container var1) {
      Insets var3 = var1.getInsets();
      Dimension var2;
      (var2 = new Dimension(MP3PlayerUICompact.a(this.a).getPreferredSize())).width = var2.width + var3.left + var3.right;
      var2.height = var2.height + var3.top + var3.bottom;
      return var2;
   }

   public final Dimension minimumLayoutSize(Container var1) {
      Insets var3 = var1.getInsets();
      Dimension var2;
      (var2 = new Dimension(MP3PlayerUICompact.a(this.a).getMinimumSize())).width = var2.width + var3.left + var3.right;
      var2.height = var2.height + var3.top + var3.bottom;
      return var2;
   }

   public final void removeLayoutComponent(Component var1) {
   }

   public final void addLayoutComponent(String var1, Component var2) {
   }
}
