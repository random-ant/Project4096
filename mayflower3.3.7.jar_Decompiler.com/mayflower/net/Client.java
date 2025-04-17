package mayflower.net;

import java.io.PrintWriter;
import java.net.Socket;

public abstract class Client {
   private ClientSideServer server;
   private boolean connected;
   private Socket socket;
   private PrintWriter out;

   public boolean connect(int port) {
      return this.connect("localhost", port);
   }

   public boolean connect(String ip, int port) {
      if (this.connected) {
         this.disconnect();
      }

      this.connected = false;

      try {
         this.socket = new Socket(ip, port);
         this.out = new PrintWriter(this.socket.getOutputStream(), true);
         this.connected = true;
         this.server = new ClientSideServer(this.socket, this);
         this.server.start();
         this.onConnect();
      } catch (Exception var4) {
         var4.printStackTrace();
         this.disconnect();
      }

      return this.connected;
   }

   public void send(String message) {
      if (this.connected) {
         this.out.println(message);
      }

   }

   public void disconnect() {
      this.connected = false;
      this.server.disconnect();

      try {
         this.out.close();
         this.socket.close();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

      this.onDisconnect("Disconnected from Server");
   }

   public abstract void process(String var1);

   public abstract void onDisconnect(String var1);

   public abstract void onConnect();
}
