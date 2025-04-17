package kuusisto.tinysound.internal;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import kuusisto.tinysound.Sound;

public class StreamSound implements Sound {
   private URL dataURL;
   private long numBytesPerChannel;
   private Mixer mixer;
   private final int ID;

   public StreamSound(URL dataURL, long numBytesPerChannel, Mixer mixer, int id) throws IOException {
      this.dataURL = dataURL;
      this.numBytesPerChannel = numBytesPerChannel;
      this.mixer = mixer;
      this.ID = id;
      InputStream temp = this.dataURL.openStream();
      temp.close();
   }

   public void play() {
      this.play(1.0D);
   }

   public void play(double volume) {
      this.play(volume, 0.0D);
   }

   public void play(double volume, double pan) {
      try {
         SoundReference ref = new StreamSound.StreamSoundReference(this.dataURL.openStream(), this.numBytesPerChannel, volume, pan, this.ID);
         this.mixer.registerSoundReference(ref);
      } catch (IOException var7) {
         System.err.println("Failed to open stream for Sound");
      }

   }

   public void stop() {
      this.mixer.unRegisterSoundReference(this.ID);
   }

   public void unload() {
      this.mixer.unRegisterSoundReference(this.ID);
      this.mixer = null;
      this.dataURL = null;
   }

   private static class StreamSoundReference implements SoundReference {
      public final int SOUND_ID;
      private InputStream data;
      private long numBytesPerChannel;
      private long position;
      private double volume;
      private double pan;
      private byte[] buf;
      private byte[] skipBuf;

      public StreamSoundReference(InputStream data, long numBytesPerChannel, double volume, double pan, int soundID) {
         this.data = data;
         this.numBytesPerChannel = numBytesPerChannel;
         this.volume = volume >= 0.0D ? volume : 1.0D;
         this.pan = pan >= -1.0D && pan <= 1.0D ? pan : 0.0D;
         this.position = 0L;
         this.buf = new byte[4];
         this.skipBuf = new byte[20];
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
         return this.numBytesPerChannel - this.position;
      }

      public void skipBytes(long num) {
         if (this.position + num >= this.numBytesPerChannel) {
            this.position = this.numBytesPerChannel;
         } else {
            long numSkip = num * 2L;
            int tmpRead = 0;
            long numRead = 0L;

            try {
               while(numRead < numSkip && tmpRead != -1) {
                  long remaining = numSkip - numRead;
                  int len = remaining > (long)this.skipBuf.length ? this.skipBuf.length : (int)remaining;
                  tmpRead = this.data.read(this.skipBuf, 0, len);
                  numRead += (long)tmpRead;
               }
            } catch (IOException var11) {
               this.position = this.numBytesPerChannel;
            }

            if (tmpRead == -1) {
               this.position = this.numBytesPerChannel;
            } else {
               this.position += num;
            }

         }
      }

      public void nextTwoBytes(int[] data, boolean bigEndian) {
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

      }

      public void dispose() {
         this.position = this.numBytesPerChannel;

         try {
            this.data.close();
         } catch (IOException var2) {
         }

         this.buf = null;
         this.skipBuf = null;
      }
   }
}
