package kuusisto.tinysound.internal;

import kuusisto.tinysound.Sound;

public class MemSound implements Sound {
   private byte[] left;
   private byte[] right;
   private Mixer mixer;
   private final int ID;

   public MemSound(byte[] left, byte[] right, Mixer mixer, int id) {
      this.left = left;
      this.right = right;
      this.mixer = mixer;
      this.ID = id;
   }

   public void play() {
      this.play(1.0D);
   }

   public void play(double volume) {
      this.play(volume, 0.0D);
   }

   public void play(double volume, double pan) {
      SoundReference ref = new MemSound.MemSoundReference(this.left, this.right, volume, pan, this.ID);
      this.mixer.registerSoundReference(ref);
   }

   public void stop() {
      this.mixer.unRegisterSoundReference(this.ID);
   }

   public void unload() {
      this.mixer.unRegisterSoundReference(this.ID);
      this.mixer = null;
      this.left = null;
      this.right = null;
   }

   private static class MemSoundReference implements SoundReference {
      public final int SOUND_ID;
      private byte[] left;
      private byte[] right;
      private int position;
      private double volume;
      private double pan;

      public MemSoundReference(byte[] left, byte[] right, double volume, double pan, int soundID) {
         this.left = left;
         this.right = right;
         this.volume = volume >= 0.0D ? volume : 1.0D;
         this.pan = pan >= -1.0D && pan <= 1.0D ? pan : 0.0D;
         this.position = 0;
         this.SOUND_ID = soundID;
      }

      public int getSoundID() {
         return this.SOUND_ID;
      }

      public double getVolume() {
         return this.volume;
      }

      public double getPan() {
         return this.pan;
      }

      public long bytesAvailable() {
         return (long)(this.left.length - this.position);
      }

      public synchronized void skipBytes(long num) {
         this.position = (int)((long)this.position + num);
      }

      public void nextTwoBytes(int[] data, boolean bigEndian) {
         if (bigEndian) {
            data[0] = this.left[this.position] << 8 | this.left[this.position + 1] & 255;
            data[1] = this.right[this.position] << 8 | this.right[this.position + 1] & 255;
         } else {
            data[0] = this.left[this.position + 1] << 8 | this.left[this.position] & 255;
            data[1] = this.right[this.position + 1] << 8 | this.right[this.position] & 255;
         }

         this.position += 2;
      }

      public void dispose() {
         this.position = this.left.length + 1;
         this.left = null;
         this.right = null;
      }
   }
}
