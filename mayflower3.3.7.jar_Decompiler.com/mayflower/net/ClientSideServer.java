package mayflower.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class ClientSideServer extends Thread {
   private Client client;
   private boolean running;
   private BufferedReader in;

   public ClientSideServer(Socket socket, Client client) {
      this.client = client;

      try {
         this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public void disconnect() {
      if (this.running) {
         this.running = false;

         try {
            this.in.close();
         } catch (Exception var2) {
            var2.printStackTrace();
         }

         this.client.disconnect();
      }
   }

   public void run() {
      this.running = true;

      while(this.running) {
         try {
            String message = this.in.readLine();
            if (message == null) {
               break;
            }

            this.client.process(message);
         } catch (SocketException var3) {
         } catch (Exception var4) {
            var4.printStackTrace();
            this.running = false;
         }

         try {
            Thread.sleep(1L);
         } catch (Exception var2) {
            var2.printStackTrace();
         }
      }

      this.disconnect();
   }
}
