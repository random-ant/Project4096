package mayflower.ui;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import mayflower.Actor;
import mayflower.Mayflower;
import mayflower.MayflowerImage;
import mayflower.event.EventListener;

public class Button extends Actor {
   private String clickAction;
   private String onHoverAction;
   private String offHoverAction;
   private String releaseAction;
   private MayflowerImage img;
   private MayflowerImage clickImg;
   private MayflowerImage hoverImg;
   private List<EventListener> listeners;

   public Button(String img, String action) {
      this.setDefaultImage(img);
      this.clickAction = action;
      this.listeners = new LinkedList();
      this.setImage(this.img);
   }

   public Button(String defaultImg, String clickImg, String action) {
      this(defaultImg, action);
      this.setClickImage(clickImg);
   }

   public Button setClickAction(String action) {
      this.clickAction = action;
      return this;
   }

   public Button setOnHoverAction(String action) {
      this.onHoverAction = action;
      return this;
   }

   public Button setOffHoverAction(String action) {
      this.offHoverAction = action;
      return this;
   }

   public Button setReleaseAction(String action) {
      this.releaseAction = action;
      return this;
   }

   public Button setDefaultImage(String img) {
      this.img = new MayflowerImage(img);
      return this;
   }

   public Button setClickImage(String img) {
      this.clickImg = new MayflowerImage(img);
      return this;
   }

   public Button setHoverImage(String img) {
      this.hoverImg = new MayflowerImage(img);
      return this;
   }

   public Button addEventListener(EventListener listener) {
      this.listeners.add(listener);
      return this;
   }

   public Button removeEventListener(EventListener listener) {
      this.listeners.remove(listener);
      return this;
   }

   public Button clearEventListeners() {
      this.listeners.clear();
      return this;
   }

   private void triggerEvent(String event) {
      Iterator var3 = this.listeners.iterator();

      while(var3.hasNext()) {
         EventListener listener = (EventListener)var3.next();
         listener.onEvent(event);
      }

      this.onEvent(event);
   }

   public void act() {
      if (Mayflower.mouseClicked(this)) {
         if (this.clickAction != null) {
            this.triggerEvent(this.clickAction);
         }

         if (this.clickImg != null) {
            this.setImage(this.clickImg);
         }
      } else if (Mayflower.wasMouseClicked(this)) {
         if (this.releaseAction != null) {
            this.triggerEvent(this.releaseAction);
         }

         if (this.img != null) {
            this.setImage(this.img);
         }
      }

      if (Mayflower.mouseHovered(this)) {
         if (this.onHoverAction != null) {
            this.triggerEvent(this.onHoverAction);
         }

         if (this.clickImg != null && Mayflower.mouseDown(this)) {
            this.setImage(this.clickImg);
         } else if (this.hoverImg != null) {
            this.setImage(this.hoverImg);
         }
      } else if (Mayflower.wasMouseHovered(this)) {
         if (this.offHoverAction != null) {
            this.triggerEvent(this.offHoverAction);
         }

         if (this.img != null) {
            this.setImage(this.img);
         }
      }

   }

   public void onEvent(String action) {
   }
}
