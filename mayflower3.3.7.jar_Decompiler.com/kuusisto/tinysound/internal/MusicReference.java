package kuusisto.tinysound.internal;

public interface MusicReference {
   boolean getPlaying();

   boolean getLoop();

   long getPosition();

   long getLoopPosition();

   double getVolume();

   double getPan();

   void setPlaying(boolean var1);

   void setLoop(boolean var1);

   void setPosition(long var1);

   void setLoopPosition(long var1);

   void setVolume(double var1);

   void setPan(double var1);

   long bytesAvailable();

   boolean done();

   void skipBytes(long var1);

   void nextTwoBytes(int[] var1, boolean var2);

   void dispose();
}
