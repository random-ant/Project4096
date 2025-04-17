package kuusisto.tinysound.internal;

import java.util.Arrays;

public class ByteList {
   private int numBytes;
   private byte[] data;

   public ByteList() {
      this(10);
   }

   public ByteList(int startSize) {
      startSize = startSize >= 0 ? startSize : 10;
      this.data = new byte[startSize];
      this.numBytes = 0;
   }

   public void add(byte b) {
      if (this.numBytes >= this.data.length && this.numBytes >= Integer.MAX_VALUE) {
         throw new RuntimeException("Array reached maximum size");
      } else {
         if (this.numBytes >= this.data.length) {
            long tmp = (long)(this.data.length * 2);
            int newSize = tmp > 2147483647L ? Integer.MAX_VALUE : (int)tmp;
            this.data = Arrays.copyOf(this.data, newSize);
         }

         this.data[this.numBytes] = b;
         ++this.numBytes;
      }
   }

   public byte get(int i) {
      if (i >= 0 && i <= this.numBytes) {
         return this.data[i];
      } else {
         throw new ArrayIndexOutOfBoundsException(i);
      }
   }

   public int size() {
      return this.numBytes;
   }

   public byte[] asArray() {
      return Arrays.copyOf(this.data, this.numBytes);
   }

   public void clear() {
      this.data = new byte[10];
      this.numBytes = 0;
   }
}
