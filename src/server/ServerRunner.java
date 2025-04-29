package server;

import mayflower.net.Server;
import mayflower.net.ServerGUI;

/**
 * Run the server here.
 */
public class ServerRunner {
   public static void main(String[] args) {
      try {
         Server server = new GameServer(1234);
         new ServerGUI(server);
         server.start();
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
