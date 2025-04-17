package kuusisto.tinysound.internal;

import java.util.concurrent.atomic.AtomicBoolean;
import javax.sound.sampled.SourceDataLine;
import kuusisto.tinysound.TinySound;

public class UpdateRunner implements Runnable {
   private AtomicBoolean running = new AtomicBoolean();
   private SourceDataLine outLine;
   private Mixer mixer;

   public UpdateRunner(Mixer mixer, SourceDataLine outLine) {
      this.mixer = mixer;
      this.outLine = outLine;
   }

   public void stop() {
      this.running.set(false);
   }

   public void run() {
      this.running.set(true);
      int bufSize = (int)TinySound.FORMAT.getFrameRate() * TinySound.FORMAT.getFrameSize();
      byte[] audioBuffer = new byte[bufSize];
      int maxFramesPerUpdate = (int)(TinySound.FORMAT.getFrameRate() / 1000.0F * 25.0F);
      int numBytesRead = 0;
      double framesAccrued = 0.0D;
      long lastUpdate = System.nanoTime();

      while(this.running.get()) {
         long currTime = System.nanoTime();
         double delta = (double)(currTime - lastUpdate);
         double secDelta = delta / 1.0E9D;
         framesAccrued += secDelta * (double)TinySound.FORMAT.getFrameRate();
         int framesToRead = (int)framesAccrued;
         int framesToSkip = 0;
         if (framesToRead > maxFramesPerUpdate) {
            framesToSkip = framesToRead - maxFramesPerUpdate;
            framesToRead = maxFramesPerUpdate;
         }

         int bytesToRead;
         if (framesToSkip > 0) {
            bytesToRead = framesToSkip * TinySound.FORMAT.getFrameSize();
            this.mixer.skip(bytesToRead);
         }

         if (framesToRead > 0) {
            bytesToRead = framesToRead * TinySound.FORMAT.getFrameSize();
            int tmpBytesRead = this.mixer.read(audioBuffer, numBytesRead, bytesToRead);
            numBytesRead += tmpBytesRead;
            int remaining = bytesToRead - tmpBytesRead;

            for(int i = 0; i < remaining; ++i) {
               audioBuffer[numBytesRead + i] = 0;
            }

            numBytesRead += remaining;
         }

         framesAccrued -= (double)(framesToRead + framesToSkip);
         if (numBytesRead > 0) {
            this.outLine.write(audioBuffer, 0, numBytesRead);
            numBytesRead = 0;
         }

         lastUpdate = currTime;

         try {
            Thread.sleep(1L);
         } catch (InterruptedException var21) {
         }
      }

   }
}
