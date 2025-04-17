package kuusisto.tinysound.internal;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import kuusisto.tinysound.Music;
import kuusisto.tinysound.TinySound;

public class StreamMusic implements Music {
   private URL dataURL;
   private Mixer mixer;
   private MusicReference reference;

   public StreamMusic(URL dataURL, long numBytesPerChannel, Mixer mixer) throws IOException {
      this.dataURL = dataURL;
      this.mixer = mixer;
      this.reference = new StreamMusic.StreamMusicReference(this.dataURL, false, false, 0L, 0L, numBytesPerChannel, 1.0D, 0.0D);
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
      this.dataURL = null;
      this.reference = null;
   }

   private static class StreamMusicReference implements MusicReference {
      private URL url;
      private InputStream data;
      private long numBytesPerChannel;
      private byte[] buf;
      private byte[] skipBuf;
      private boolean playing;
      private boolean loop;
      private long loopPosition;
      private long position;
      private double volume;
      private double pan;

      public StreamMusicReference(URL dataURL, boolean playing, boolean loop, long loopPosition, long position, long numBytesPerChannel, double volume, double pan) throws IOException {
         this.url = dataURL;
         this.playing = playing;
         this.loop = loop;
         this.loopPosition = loopPosition;
         this.position = position;
         this.numBytesPerChannel = numBytesPerChannel;
         this.volume = volume;
         this.pan = pan;
         this.buf = new byte[4];
         this.skipBuf = new byte[50];
         this.data = this.url.openStream();
      }

      public synchronized boolean getPlaying() {
         return this.playing;
      }

      public synchronized boolean getLoop() {
         return this.loop;
      }

      public synchronized long getPosition() {
         return this.position;
      }

      public synchronized long getLoopPosition() {
         return this.loopPosition;
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
         if (position >= 0L && position < this.numBytesPerChannel) {
            if (position >= this.position) {
               this.skipBytes(position - this.position);
            } else {
               try {
                  this.data.close();
               } catch (IOException var5) {
               }

               try {
                  this.data = this.url.openStream();
                  this.position = 0L;
                  this.skipBytes(position);
               } catch (IOException var4) {
                  System.err.println("Failed to open stream for StreamMusic");
                  this.playing = false;
               }
            }
         }

      }

      public synchronized void setLoopPosition(long loopPosition) {
         if (loopPosition >= 0L && loopPosition < this.numBytesPerChannel) {
            this.loopPosition = loopPosition;
         }

      }

      public synchronized void setVolume(double volume) {
         this.volume = volume;
      }

      public synchronized void setPan(double pan) {
         this.pan = pan;
      }

      public synchronized long bytesAvailable() {
         return this.numBytesPerChannel - this.position;
      }

      public synchronized boolean done() {
         long available = this.numBytesPerChannel - this.position;
         return available <= 0L && !this.playing;
      }

      public synchronized void skipBytes(long num) {
         long numSkip;
         long remaining;
         if (this.position + num >= this.numBytesPerChannel) {
            if (!this.loop) {
               this.position += num;
               this.playing = false;
            } else {
               numSkip = this.numBytesPerChannel - this.loopPosition;
               long bytesOver = this.position + num - this.numBytesPerChannel;
               remaining = this.loopPosition + bytesOver % numSkip;
               this.setPosition(remaining);
            }
         } else {
            numSkip = num * 2L;
            int tmpRead = 0;
            int numRead = 0;

            try {
               while((long)numRead < numSkip && tmpRead != -1) {
                  remaining = numSkip - (long)numRead;
                  int len = remaining > (long)this.skipBuf.length ? this.skipBuf.length : (int)remaining;
                  tmpRead = this.data.read(this.skipBuf, 0, len);
                  numRead += tmpRead;
               }
            } catch (IOException var10) {
               this.position = this.numBytesPerChannel;
               this.playing = false;
            }

            if (tmpRead == -1) {
               this.position = this.numBytesPerChannel;
               this.playing = false;
            } else {
               this.position += num;
            }

         }
      }

      public synchronized void nextTwoBytes(int[] data, boolean bigEndian) {
         int tmpRead = 0;
         int numRead = 0;

         try {
            while(numRead < this.buf.length && tmpRead != -1) {
               tmpRead = this.data.read(this.buf, numRead, this.buf.length - numRead);
               numRead += tmpRead;
            }
         } catch (IOException var6) {
            this.position = this.numBytesPerChannel;
            System.err.println("Failed reading bytes for stream sound");
         }

         if (bigEndian) {
            data[0] = this.buf[0] << 8 | this.buf[1] & 255;
            data[1] = this.buf[2] << 8 | this.buf[3] & 255;
         } else {
            data[0] = this.buf[1] << 8 | this.buf[0] & 255;
            data[1] = this.buf[3] << 8 | this.buf[2] & 255;
         }

         if (tmpRead == -1) {
            this.position = this.numBytesPerChannel;
         } else {
            this.position += 2L;
         }

         if (this.position >= this.numBytesPerChannel) {
            if (this.loop) {
               this.setPosition(this.loopPosition);
            } else {
               this.playing = false;
            }
         }

      }

      public synchronized void dispose() {
         this.playing = false;
         this.position = this.numBytesPerChannel;
         this.url = null;

         try {
            this.data.close();
         } catch (IOException var2) {
         }

      }
   }
}
