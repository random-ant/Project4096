package kuusisto.tinysound;

public interface Music {
   void play(boolean var1);

   void play(boolean var1, double var2);

   void play(boolean var1, double var2, double var4);

   void stop();

   void pause();

   void resume();

   void rewind();

   void rewindToLoopPosition();

   boolean playing();

   boolean done();

   boolean loop();

   void setLoop(boolean var1);

   int getLoopPositionByFrame();

   double getLoopPositionBySeconds();

   void setLoopPositionByFrame(int var1);

   void setLoopPositionBySeconds(double var1);

   double getVolume();

   void setVolume(double var1);

   double getPan();

   void setPan(double var1);

   void unload();
}
