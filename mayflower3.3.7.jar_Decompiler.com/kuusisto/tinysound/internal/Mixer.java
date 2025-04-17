package kuusisto.tinysound.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Mixer {
   private List<MusicReference> musics = new ArrayList();
   private List<SoundReference> sounds = new ArrayList();
   private double globalVolume = 1.0D;
   private int[] dataBuf = new int[2];

   public synchronized double getVolume() {
      return this.globalVolume;
   }

   public synchronized void setVolume(double volume) {
      if (volume >= 0.0D) {
         this.globalVolume = volume;
      }

   }

   public synchronized void registerMusicReference(MusicReference music) {
      this.musics.add(music);
   }

   public synchronized void registerSoundReference(SoundReference sound) {
      this.sounds.add(sound);
   }

   public synchronized void unRegisterMusicReference(MusicReference music) {
      this.musics.remove(music);
   }

   public synchronized void unRegisterSoundReference(int soundID) {
      for(int i = this.sounds.size() - 1; i >= 0; --i) {
         if (((SoundReference)this.sounds.get(i)).getSoundID() == soundID) {
            ((SoundReference)this.sounds.remove(i)).dispose();
         }
      }

   }

   public synchronized void clearMusic() {
      this.musics.clear();
   }

   public synchronized void clearSounds() {
      Iterator var2 = this.sounds.iterator();

      while(var2.hasNext()) {
         SoundReference s = (SoundReference)var2.next();
         s.dispose();
      }

      this.sounds.clear();
   }

   public synchronized int read(byte[] data, int offset, int length) {
      int numRead = 0;
      boolean bytesRead = true;

      for(int i = offset; i < length + offset && bytesRead; i += 4) {
         bytesRead = false;
         double leftValue = 0.0D;
         double rightValue = 0.0D;

         int finalLeftValue;
         double volume;
         double leftCurr;
         double rightCurr;
         double pan;
         double ll;
         double lr;
         double rl;
         double rr;
         double tmpL;
         double tmpR;
         for(finalLeftValue = 0; finalLeftValue < this.musics.size(); ++finalLeftValue) {
            MusicReference music = (MusicReference)this.musics.get(finalLeftValue);
            if (music.getPlaying() && music.bytesAvailable() > 0L) {
               music.nextTwoBytes(this.dataBuf, false);
               volume = music.getVolume() * this.globalVolume;
               leftCurr = (double)this.dataBuf[0] * volume;
               rightCurr = (double)this.dataBuf[1] * volume;
               pan = music.getPan();
               if (pan != 0.0D) {
                  ll = pan <= 0.0D ? 1.0D : 1.0D - pan;
                  lr = pan <= 0.0D ? Math.abs(pan) : 0.0D;
                  rl = pan >= 0.0D ? pan : 0.0D;
                  rr = pan >= 0.0D ? 1.0D : 1.0D - Math.abs(pan);
                  tmpL = ll * leftCurr + lr * rightCurr;
                  tmpR = rl * leftCurr + rr * rightCurr;
                  leftCurr = tmpL;
                  rightCurr = tmpR;
               }

               leftValue += leftCurr;
               rightValue += rightCurr;
               bytesRead = true;
            }
         }

         for(finalLeftValue = this.sounds.size() - 1; finalLeftValue >= 0; --finalLeftValue) {
            SoundReference sound = (SoundReference)this.sounds.get(finalLeftValue);
            if (sound.bytesAvailable() > 0L) {
               sound.nextTwoBytes(this.dataBuf, false);
               volume = sound.getVolume() * this.globalVolume;
               leftCurr = (double)this.dataBuf[0] * volume;
               rightCurr = (double)this.dataBuf[1] * volume;
               pan = sound.getPan();
               if (pan != 0.0D) {
                  ll = pan <= 0.0D ? 1.0D : 1.0D - pan;
                  lr = pan <= 0.0D ? Math.abs(pan) : 0.0D;
                  rl = pan >= 0.0D ? pan : 0.0D;
                  rr = pan >= 0.0D ? 1.0D : 1.0D - Math.abs(pan);
                  tmpL = ll * leftCurr + lr * rightCurr;
                  tmpR = rl * leftCurr + rr * rightCurr;
                  leftCurr = tmpL;
                  rightCurr = tmpR;
               }

               leftValue += leftCurr;
               rightValue += rightCurr;
               bytesRead = true;
               if (sound.bytesAvailable() <= 0L) {
                  ((SoundReference)this.sounds.remove(finalLeftValue)).dispose();
               }
            } else {
               ((SoundReference)this.sounds.remove(finalLeftValue)).dispose();
            }
         }

         if (bytesRead) {
            finalLeftValue = (int)leftValue;
            int finalRightValue = (int)rightValue;
            if (finalLeftValue > 32767) {
               finalLeftValue = 32767;
            } else if (finalLeftValue < -32768) {
               finalLeftValue = -32768;
            }

            if (finalRightValue > 32767) {
               finalRightValue = 32767;
            } else if (finalRightValue < -32768) {
               finalRightValue = -32768;
            }

            data[i + 1] = (byte)(finalLeftValue >> 8 & 255);
            data[i] = (byte)(finalLeftValue & 255);
            data[i + 3] = (byte)(finalRightValue >> 8 & 255);
            data[i + 2] = (byte)(finalRightValue & 255);
            numRead += 4;
         }
      }

      return numRead;
   }

   public synchronized void skip(int numBytes) {
      int s;
      for(s = 0; s < this.musics.size(); ++s) {
         MusicReference music = (MusicReference)this.musics.get(s);
         if (music.getPlaying() && music.bytesAvailable() > 0L) {
            music.skipBytes((long)numBytes);
         }
      }

      for(s = this.sounds.size() - 1; s >= 0; --s) {
         SoundReference sound = (SoundReference)this.sounds.get(s);
         if (sound.bytesAvailable() > 0L) {
            sound.skipBytes((long)numBytes);
            if (sound.bytesAvailable() <= 0L) {
               ((SoundReference)this.sounds.remove(s)).dispose();
            }
         } else {
            ((SoundReference)this.sounds.remove(s)).dispose();
         }
      }

   }
}
