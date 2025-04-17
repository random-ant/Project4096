package kuusisto.tinysound.internal;

import kuusisto.tinysound.Music;
import kuusisto.tinysound.TinySound;

public class MemMusic implements Music {
   private byte[] left;
   private byte[] right;
   private Mixer mixer;
   private MusicReference reference;

   public MemMusic(byte[] left, byte[] right, Mixer mixer) {
      this.left = left;
      this.right = right;
      this.mixer = mixer;
      this.reference = new MemMusic.MemMusicReference(this.left, this.right, false, false, 0, 0, 1.0D, 0.0D);
      this.mixer.registerMusicReference(this.reference);
   }

   public void play(boolean loop) {
      this.reference.setLoop(loop);
      this.reference.setPlaying(true);
   }

   public void play(boolean loop, double volume) {
      this.setLoop(loop);
      this.setVolume(volume);
      this.reference.setPlaying(true);
   }

   public void play(boolean loop, double volume, double pan) {
      this.setLoop(loop);
      this.setVolume(volume);
      this.setPan(pan);
      this.reference.setPlaying(true);
   }

   public void stop() {
      this.reference.setPlaying(false);
      this.rewind();
   }

   public void pause() {
      this.reference.setPlaying(false);
   }

   public void resume() {
      this.reference.setPlaying(true);
   }

   public void rewind() {
      this.reference.setPosition(0L);
   }

   public void rewindToLoopPosition() {
      long byteIndex = this.reference.getLoopPosition();
      this.reference.setPosition(byteIndex);
   }

   public boolean playing() {
      return this.reference.getPlaying();
   }

   public boolean done() {
      return this.reference.done();
   }

   public boolean loop() {
      return this.reference.getLoop();
   }

   public void setLoop(boolean loop) {
      this.reference.setLoop(loop);
   }

   public int getLoopPositionByFrame() {
      int bytesPerChannelForFrame = TinySound.FORMAT.getFrameSize() / TinySound.FORMAT.getChannels();
      long byteIndex = this.reference.getLoopPosition();
      return (int)(byteIndex / (long)bytesPerChannelForFrame);
   }

   public double getLoopPositionBySeconds() {
      int bytesPerChannelForFrame = TinySound.FORMAT.getFrameSize() / TinySound.FORMAT.getChannels();
      long byteIndex = this.reference.getLoopPosition();
      return (double)((float)byteIndex / (TinySound.FORMAT.getFrameRate() * (float)bytesPerChannelForFrame));
   }

   public void setLoopPositionByFrame(int frameIndex) {
      int bytesPerChannelForFrame = TinySound.FORMAT.getFrameSize() / TinySound.FORMAT.getChannels();
      long byteIndex = (long)(frameIndex * bytesPerChannelForFrame);
      this.reference.setLoopPosition(byteIndex);
   }

   public void setLoopPositionBySeconds(double seconds) {
      int bytesPerChannelForFrame = TinySound.FORMAT.getFrameSize() / TinySound.FORMAT.getChannels();
      long byteIndex = (long)(seconds * (double)TinySound.FORMAT.getFrameRate()) * (long)bytesPerChannelForFrame;
      this.reference.setLoopPosition(byteIndex);
   }

   public double getVolume() {
      return this.reference.getVolume();
   }

   public void setVolume(double volume) {
      if (volume >= 0.0D) {
         this.reference.setVolume(volume);
      }

   }

   public double getPan() {
      return this.reference.getPan();
   }

   public void setPan(double pan) {
      if (pan >= -1.0D && pan <= 1.0D) {
         this.reference.setPan(pan);
      }

   }

   public void unload() {
      this.mixer.unRegisterMusicReference(this.reference);
      this.reference.dispose();
      this.mixer = null;
      this.left = null;
      this.right = null;
      this.reference = null;
   }

   private static class MemMusicReference implements MusicReference {
      private byte[] left;
      private byte[] right;
      private boolean playing;
      private boolean loop;
      private int loopPosition;
      private int position;
      private double volume;
      private double pan;

      public MemMusicReference(byte[] left, byte[] right, boolean playing, boolean loop, int loopPosition, int position, double volume, double pan) {
         this.left = left;
         this.right = right;
         this.playing = playing;
         this.loop = loop;
         this.loopPosition = loopPosition;
         this.position = position;
         this.volume = volume;
         this.pan = pan;
      }

      public synchronized boolean getPlaying() {
         return this.playing;
      }

      public synchronized boolean getLoop() {
         return this.loop;
      }

      public synchronized long getPosition() {
         return (long)this.position;
      }

      public synchronized long getLoopPosition() {
         return (long)this.loopPosition;
      }

      public synchronized double getVolume() {
         return this.volume;
      }

      public synchronized double getPan() {
         return this.pan;
      }

      public synchronized void setPlaying(boolean playing) {
         this.playing = playing;
      }

      public synchronized void setLoop(boolean loop) {
         this.loop = loop;
      }

      public synchronized void setPosition(long position) {
         if (position >= 0L && position < (long)this.left.length) {
            this.position = (int)position;
         }

      }

      public synchronized void setLoopPosition(long loopPosition) {
         if (loopPosition >= 0L && loopPosition < (long)this.left.length) {
            this.loopPosition = (int)loopPosition;
         }

      }

      public synchronized void setVolume(double volume) {
         this.volume = volume;
      }

      public synchronized void setPan(double pan) {
         this.pan = pan;
      }

      public synchronized long bytesAvailable() {
         return (long)(this.left.length - this.position);
      }

      public synchronized boolean done() {
         long available = (long)(this.left.length - this.position);
         return available <= 0L && !this.playing;
      }

      public synchronized void skipBytes(long num) {
         for(int i = 0; (long)i < num; ++i) {
            ++this.position;
            if (this.position >= this.left.length) {
               if (this.loop) {
                  this.position = this.loopPosition;
               } else {
                  this.playing = false;
               }
            }
         }

      }

      public synchronized void nextTwoBytes(int[] data, boolean bigEndian) {
         if (bigEndian) {
            data[0] = this.left[this.position] << 8 | this.left[this.position + 1] & 255;
            data[1] = this.right[this.position] << 8 | this.right[this.position + 1] & 255;
         } else {
            data[0] = this.left[this.position + 1] << 8 | this.left[this.position] & 255;
            data[1] = this.right[this.position + 1] << 8 | this.right[this.position] & 255;
         }

         this.position += 2;
         if (this.position >= this.left.length) {
            if (this.loop) {
               this.position = this.loopPosition;
            } else {
               this.playing = false;
            }
         }

      }

      public synchronized void dispose() {
         this.playing = false;
         this.position = this.left.length + 1;
         this.left = null;
         this.right = null;
      }
   }
}
