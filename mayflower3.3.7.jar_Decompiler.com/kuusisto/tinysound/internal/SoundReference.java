package kuusisto.tinysound.internal;

public interface SoundReference {
   int getSoundID();

   double getVolume();

   double getPan();

   long bytesAvailable();

   void skipBytes(long var1);

   void nextTwoBytes(int[] var1, boolean var2);

   void dispose();
}
