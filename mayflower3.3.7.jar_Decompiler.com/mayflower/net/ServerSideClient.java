package mayflower.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerSideClient extends Thread {
   private int id;
   private boolean running;
   private Socket client;
   private Server server;
   private PrintWriter out;
   private BufferedReader in;

   public ServerSideClient(int id, Socket client, Server server) {
      this.id = id;
      this.client = client;
      this.server = server;

      try {
         this.out = new PrintWriter(client.getOutputStream(), true);
         this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public int getClientId() {
      return this.id;
   }

   public void send(String message) {
      this.out.println(message);
   }

   public void disconnect() {
      if (this.running) {
         this.running = false;

         try {
            this.in.close();
            this.out.close();
            this.client.close();
         } catch (Exception var2) {
            var2.printStackTrace();
         }

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

            this.server.process(this.getClientId(), message);
         } catch (Exception var3) {
            var3.printStackTrace();
            this.running = false;
            break;
         }

         try {
            Thread.sleep(1L);
         } catch (Exception var2) {
            var2.printStackTrace();
         }
      }

      this.disconnect();
      this.server.disconnect(this.id);
   }

   public boolean equals(Object other) {
      if (other != null && other instanceof ServerSideClient) {
         return ((ServerSideClient)other).getClientId() == this.id;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.id;
   }
}
