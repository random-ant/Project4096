package mayflower;

public class Timer {
   private int goal;
   private long start;

   public Timer() {
      this.reset();
      this.goal = 0;
   }

   public Timer(int goal) {
      this.reset();
      this.goal = goal;
   }

   public void set(int goal) {
      this.reset();
      this.goal = goal;
   }

   public void adjust(int diff) {
      this.goal += diff;
   }

   public boolean isDone() {
      return this.getTimeLeft() <= 0L;
   }

   public long getTimeLeft() {
      return (long)this.goal + this.start - Mayflower.getTime();
   }

   public boolean isTimeLeft(int time) {
      return this.getTimeLeft() >= 0L;
   }

   public void reset() {
      this.start = Mayflower.getTime();
   }
}
