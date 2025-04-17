package mayflower.util;

public class FastTrig {
   private static double reduceSinAngle(double radians) {
      radians %= 6.283185307179586D;
      if (Math.abs(radians) > 3.141592653589793D) {
         radians -= 6.283185307179586D;
      }

      if (Math.abs(radians) > 1.5707963267948966D) {
         radians = 3.141592653589793D - radians;
      }

      return radians;
   }

   public static double sin(double radians) {
      radians = reduceSinAngle(radians);
      return Math.abs(radians) <= 0.7853981633974483D ? Math.sin(radians) : Math.cos(1.5707963267948966D - radians);
   }

   public static double cos(double radians) {
      return sin(radians + 1.5707963267948966D);
   }
}
