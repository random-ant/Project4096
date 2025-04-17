package kuusisto.tinysound;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.DataLine.Info;
import kuusisto.tinysound.internal.ByteList;
import kuusisto.tinysound.internal.MemMusic;
import kuusisto.tinysound.internal.MemSound;
import kuusisto.tinysound.internal.Mixer;
import kuusisto.tinysound.internal.StreamInfo;
import kuusisto.tinysound.internal.StreamMusic;
import kuusisto.tinysound.internal.StreamSound;
import kuusisto.tinysound.internal.UpdateRunner;

public class TinySound {
   public static final String VERSION = "1.1.1";
   public static final AudioFormat FORMAT;
   private static Mixer mixer;
   private static SourceDataLine outLine;
   private static boolean inited;
   private static UpdateRunner autoUpdater;
   private static int soundCount;

   static {
      FORMAT = new AudioFormat(Encoding.PCM_SIGNED, 44100.0F, 16, 2, 4, 44100.0F, false);
      inited = false;
      soundCount = 0;
   }

   public static void init() {
      if (!inited) {
         Info info = new Info(SourceDataLine.class, FORMAT);
         if (!AudioSystem.isLineSupported(info)) {
            System.err.println("Unsupported output format!");
         } else {
            outLine = tryGetLine();
            if (outLine == null) {
               System.err.println("Output line unavailable!");
            } else {
               outLine.start();
               finishInit();
            }
         }
      }
   }

   public static void init(javax.sound.sampled.Mixer.Info info) throws LineUnavailableException, SecurityException, IllegalArgumentException {
      if (!inited) {
         javax.sound.sampled.Mixer mixer = AudioSystem.getMixer(info);
         Info lineInfo = new Info(SourceDataLine.class, FORMAT);
         outLine = (SourceDataLine)mixer.getLine(lineInfo);
         outLine.open(FORMAT);
         outLine.start();
         finishInit();
      }
   }

   private static void finishInit() {
      mixer = new Mixer();
      autoUpdater = new UpdateRunner(mixer, outLine);
      Thread updateThread = new Thread(autoUpdater);

      try {
         updateThread.setDaemon(true);
         updateThread.setPriority(10);
      } catch (Exception var2) {
      }

      inited = true;
      updateThread.start();
      Thread.yield();
   }

   public static void shutdown() {
      if (inited) {
         inited = false;
         autoUpdater.stop();
         autoUpdater = null;
         outLine.stop();
         outLine.flush();
         mixer.clearMusic();
         mixer.clearSounds();
         mixer = null;
      }
   }

   public static boolean isInitialized() {
      return inited;
   }

   public static double getGlobalVolume() {
      return !inited ? -1.0D : mixer.getVolume();
   }

   public static void setGlobalVolume(double volume) {
      if (inited) {
         mixer.setVolume(volume);
      }
   }

   public static Music loadMusic(String name) {
      return loadMusic(name, false);
   }

   public static Music loadMusic(String name, boolean streamFromFile) {
      if (!inited) {
         System.err.println("TinySound not initialized!");
         return null;
      } else if (name == null) {
         return null;
      } else {
         if (!name.startsWith("/")) {
            name = "/" + name;
         }

         URL url = TinySound.class.getResource(name);
         if (url == null) {
            System.err.println("Unable to find resource " + name + "!");
            return null;
         } else {
            return loadMusic(url, streamFromFile);
         }
      }
   }

   public static Music loadMusic(File file) {
      return loadMusic(file, false);
   }

   public static Music loadMusic(File file, boolean streamFromFile) {
      if (!inited) {
         System.err.println("TinySound not initialized!");
         return null;
      } else if (file == null) {
         return null;
      } else {
         URL url = null;

         try {
            url = file.toURI().toURL();
         } catch (MalformedURLException var4) {
            System.err.println("Unable to find file " + file + "!");
            return null;
         }

         return loadMusic(url, streamFromFile);
      }
   }

   public static Music loadMusic(URL url) {
      return loadMusic(url, false);
   }

   public static Music loadMusic(URL url, boolean streamFromFile) {
      if (!inited) {
         System.err.println("TinySound not initialized!");
         return null;
      } else if (url == null) {
         return null;
      } else {
         AudioInputStream audioStream = getValidAudioStream(url);
         if (audioStream == null) {
            return null;
         } else {
            byte[][] data = readAllBytes(audioStream);
            if (data == null) {
               return null;
            } else if (streamFromFile) {
               StreamInfo info = createFileStream(data);
               if (info == null) {
                  return null;
               } else {
                  StreamMusic sm = null;

                  try {
                     sm = new StreamMusic(info.URL, info.NUM_BYTES_PER_CHANNEL, mixer);
                  } catch (IOException var7) {
                     System.err.println("Failed to create StreamMusic!");
                  }

                  return sm;
               }
            } else {
               return new MemMusic(data[0], data[1], mixer);
            }
         }
      }
   }

   public static Sound loadSound(String name) {
      return loadSound(name, false);
   }

   public static Sound loadSound(String name, boolean streamFromFile) {
      if (!inited) {
         System.err.println("TinySound not initialized!");
         return null;
      } else if (name == null) {
         return null;
      } else {
         if (!name.startsWith("/")) {
            name = "/" + name;
         }

         URL url = TinySound.class.getResource(name);
         if (url == null) {
            System.err.println("Unable to find resource " + name + "!");
            return null;
         } else {
            return loadSound(url, streamFromFile);
         }
      }
   }

   public static Sound loadSound(File file) {
      return loadSound(file, false);
   }

   public static Sound loadSound(File file, boolean streamFromFile) {
      if (!inited) {
         System.err.println("TinySound not initialized!");
         return null;
      } else if (file == null) {
         return null;
      } else {
         URL url = null;

         try {
            url = file.toURI().toURL();
         } catch (MalformedURLException var4) {
            System.err.println("Unable to find file " + file + "!");
            return null;
         }

         return loadSound(url, streamFromFile);
      }
   }

   public static Sound loadSound(URL url) {
      return loadSound(url, false);
   }

   public static Sound loadSound(URL url, boolean streamFromFile) {
      if (!inited) {
         System.err.println("TinySound not initialized!");
         return null;
      } else if (url == null) {
         return null;
      } else {
         AudioInputStream audioStream = getValidAudioStream(url);
         if (audioStream == null) {
            return null;
         } else {
            byte[][] data = readAllBytes(audioStream);
            if (data == null) {
               return null;
            } else if (streamFromFile) {
               StreamInfo info = createFileStream(data);
               if (info == null) {
                  return null;
               } else {
                  StreamSound ss = null;

                  try {
                     ss = new StreamSound(info.URL, info.NUM_BYTES_PER_CHANNEL, mixer, soundCount);
                     ++soundCount;
                  } catch (IOException var7) {
                     System.err.println("Failed to create StreamSound!");
                  }

                  return ss;
               }
            } else {
               ++soundCount;
               return new MemSound(data[0], data[1], mixer, soundCount);
            }
         }
      }
   }

   private static byte[][] readAllBytes(AudioInputStream stream) {
      byte[][] data = null;
      int numChannels = stream.getFormat().getChannels();
      if (numChannels == 1) {
         byte[] left = readAllBytesOneChannel(stream);
         if (left == null) {
            return null;
         }

         data = new byte[][]{left, left};
      } else if (numChannels == 2) {
         data = readAllBytesTwoChannel(stream);
      } else {
         System.err.println("Unable to read " + numChannels + " channels!");
      }

      return data;
   }

   private static byte[] readAllBytesOneChannel(AudioInputStream stream) {
      Object var1 = null;

      try {
         byte[] data = getBytes(stream);
         return data;
      } catch (IOException var11) {
         System.err.println("Error reading all bytes from stream!");
      } finally {
         try {
            stream.close();
         } catch (IOException var10) {
         }

      }

      return null;
   }

   private static byte[][] readAllBytesTwoChannel(AudioInputStream stream) {
      Object var1 = null;

      try {
         byte[] allBytes = getBytes(stream);
         byte[] left = new byte[allBytes.length / 2];
         byte[] right = new byte[allBytes.length / 2];
         int i = 0;

         for(int j = 0; i < allBytes.length; j += 2) {
            left[j] = allBytes[i];
            left[j + 1] = allBytes[i + 1];
            right[j] = allBytes[i + 2];
            right[j + 1] = allBytes[i + 3];
            i += 4;
         }

         byte[][] data = new byte[][]{left, right};
         return data;
      } catch (IOException var15) {
         System.err.println("Error reading all bytes from stream!");
      } finally {
         try {
            stream.close();
         } catch (IOException var14) {
         }

      }

      return null;
   }

   private static AudioInputStream getValidAudioStream(URL url) {
      AudioInputStream audioStream = null;

      try {
         audioStream = AudioSystem.getAudioInputStream(url);
         AudioFormat streamFormat = audioStream.getFormat();
         AudioFormat mono16 = new AudioFormat(Encoding.PCM_SIGNED, 44100.0F, 16, 1, 2, 44100.0F, false);
         AudioFormat mono8 = new AudioFormat(Encoding.PCM_SIGNED, 44100.0F, 8, 1, 1, 44100.0F, false);
         AudioFormat stereo8 = new AudioFormat(Encoding.PCM_SIGNED, 44100.0F, 8, 2, 2, 44100.0F, false);
         if (!streamFormat.matches(FORMAT) && !streamFormat.matches(mono16)) {
            if (AudioSystem.isConversionSupported(FORMAT, streamFormat)) {
               audioStream = AudioSystem.getAudioInputStream(FORMAT, audioStream);
            } else if (AudioSystem.isConversionSupported(mono16, streamFormat)) {
               audioStream = AudioSystem.getAudioInputStream(mono16, audioStream);
            } else if (!streamFormat.matches(stereo8) && !AudioSystem.isConversionSupported(stereo8, streamFormat)) {
               if (!streamFormat.matches(mono8) && !AudioSystem.isConversionSupported(mono8, streamFormat)) {
                  System.err.println("Unable to convert audio resource!");
                  System.err.println(url);
                  System.err.println(streamFormat);
                  audioStream.close();
                  return null;
               }

               if (!streamFormat.matches(mono8)) {
                  audioStream = AudioSystem.getAudioInputStream(mono8, audioStream);
               }

               audioStream = convertMono8Bit(audioStream);
            } else {
               if (!streamFormat.matches(stereo8)) {
                  audioStream = AudioSystem.getAudioInputStream(stereo8, audioStream);
               }

               audioStream = convertStereo8Bit(audioStream);
            }

            long frameLength = audioStream.getFrameLength();
            if (frameLength > 2147483647L) {
               System.err.println("Audio resource too long!");
               return null;
            } else {
               return audioStream;
            }
         } else {
            return audioStream;
         }
      } catch (UnsupportedAudioFileException var8) {
         System.err.println("Unsupported audio resource!\n" + var8.getMessage());
         return null;
      } catch (IOException var9) {
         System.err.println("Error getting resource stream!\n" + var9.getMessage());
         return null;
      }
   }

   private static AudioInputStream convertMono8Bit(AudioInputStream stream) {
      Object var1 = null;

      byte[] newData;
      label127: {
         try {
            byte[] data = getBytes(stream);
            int newNumBytes = data.length * 2;
            if (newNumBytes >= 0) {
               newData = new byte[newNumBytes];
               int i = 0;
               int j = 0;

               while(true) {
                  if (i >= data.length) {
                     break label127;
                  }

                  double floatVal = (double)data[i];
                  floatVal /= (double)(floatVal < 0.0D ? 128 : 127);
                  if (floatVal < -1.0D) {
                     floatVal = -1.0D;
                  } else if (floatVal > 1.0D) {
                     floatVal = 1.0D;
                  }

                  int val = (int)(floatVal * 32767.0D);
                  newData[j + 1] = (byte)(val >> 8 & 255);
                  newData[j] = (byte)(val & 255);
                  ++i;
                  j += 2;
               }
            }

            System.err.println("Audio resource too long!");
         } catch (IOException var18) {
            System.err.println("Error reading all bytes from stream!");
            return null;
         } finally {
            try {
               stream.close();
            } catch (IOException var17) {
            }

         }

         return null;
      }

      AudioFormat mono16 = new AudioFormat(Encoding.PCM_SIGNED, 44100.0F, 16, 1, 2, 44100.0F, false);
      return new AudioInputStream(new ByteArrayInputStream(newData), mono16, (long)(newData.length / 2));
   }

   private static AudioInputStream convertStereo8Bit(AudioInputStream stream) {
      Object var1 = null;

      byte[] newData;
      label152: {
         try {
            byte[] data = getBytes(stream);
            int newNumBytes = data.length * 2 * 2;
            if (newNumBytes >= 0) {
               newData = new byte[newNumBytes];
               int i = 0;
               int j = 0;

               while(true) {
                  if (i >= data.length) {
                     break label152;
                  }

                  double leftFloatVal = (double)data[i];
                  double rightFloatVal = (double)data[i + 1];
                  leftFloatVal /= (double)(leftFloatVal < 0.0D ? 128 : 127);
                  rightFloatVal /= (double)(rightFloatVal < 0.0D ? 128 : 127);
                  if (leftFloatVal < -1.0D) {
                     leftFloatVal = -1.0D;
                  } else if (leftFloatVal > 1.0D) {
                     leftFloatVal = 1.0D;
                  }

                  if (rightFloatVal < -1.0D) {
                     rightFloatVal = -1.0D;
                  } else if (rightFloatVal > 1.0D) {
                     rightFloatVal = 1.0D;
                  }

                  int leftVal = (int)(leftFloatVal * 32767.0D);
                  int rightVal = (int)(rightFloatVal * 32767.0D);
                  newData[j + 1] = (byte)(leftVal >> 8 & 255);
                  newData[j] = (byte)(leftVal & 255);
                  newData[j + 3] = (byte)(rightVal >> 8 & 255);
                  newData[j + 2] = (byte)(rightVal & 255);
                  i += 2;
                  j += 4;
               }
            }

            System.err.println("Audio resource too long!");
         } catch (IOException var21) {
            System.err.println("Error reading all bytes from stream!");
            return null;
         } finally {
            try {
               stream.close();
            } catch (IOException var20) {
            }

         }

         return null;
      }

      AudioFormat stereo16 = new AudioFormat(Encoding.PCM_SIGNED, 44100.0F, 16, 2, 4, 44100.0F, false);
      return new AudioInputStream(new ByteArrayInputStream(newData), stereo16, (long)(newData.length / 4));
   }

   private static byte[] getBytes(AudioInputStream stream) throws IOException {
      int bufSize = (int)FORMAT.getSampleRate() * FORMAT.getChannels() * FORMAT.getFrameSize();
      byte[] buf = new byte[bufSize];
      ByteList list = new ByteList(bufSize);
      boolean var4 = false;

      int numRead;
      while((numRead = stream.read(buf)) > -1) {
         for(int i = 0; i < numRead; ++i) {
            list.add(buf[i]);
         }
      }

      return list.asArray();
   }

   private static StreamInfo createFileStream(byte[][] data) {
      File temp = null;

      try {
         temp = File.createTempFile("tiny", "sound");
         temp.deleteOnExit();
      } catch (IOException var19) {
         System.err.println("Failed to create file for streaming!");
         return null;
      }

      URL url = null;

      try {
         url = temp.toURI().toURL();
      } catch (MalformedURLException var18) {
         System.err.println("Failed to get URL for stream file!");
         return null;
      }

      BufferedOutputStream out = null;

      try {
         out = new BufferedOutputStream(new FileOutputStream(temp), 524288);
      } catch (FileNotFoundException var17) {
         System.err.println("Failed to open stream file for writing!");
         return null;
      }

      try {
         for(int i = 0; i < data[0].length; i += 2) {
            try {
               out.write(data[0], i, 2);
               out.write(data[1], i, 2);
            } catch (IOException var20) {
               System.err.println("Failed writing bytes to stream file!");
               return null;
            }
         }

         return new StreamInfo(url, (long)data[0].length);
      } finally {
         try {
            out.close();
         } catch (IOException var16) {
            System.err.println("Failed closing stream file after writing!");
         }

      }
   }

   private static SourceDataLine tryGetLine() {
      Info lineInfo = new Info(SourceDataLine.class, FORMAT);
      javax.sound.sampled.Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();

      for(int i = 0; i < mixerInfos.length; ++i) {
         javax.sound.sampled.Mixer mixer = null;

         try {
            mixer = AudioSystem.getMixer(mixerInfos[i]);
         } catch (SecurityException var8) {
         } catch (IllegalArgumentException var9) {
         }

         if (mixer != null && mixer.isLineSupported(lineInfo)) {
            SourceDataLine line = null;

            try {
               line = (SourceDataLine)mixer.getLine(lineInfo);
               if (!line.isOpen()) {
                  line.open(FORMAT);
               }
            } catch (LineUnavailableException var6) {
            } catch (SecurityException var7) {
            }

            if (line != null && line.isOpen()) {
               return line;
            }
         }
      }

      return null;
   }
}
