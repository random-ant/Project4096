package jaco.mp3.player;

public final class i implements Cloneable {
   private h a = new h();

   public final Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new InternalError(this + ": " + var2);
      }
   }

   public final h a() {
      return this.a;
   }
}
