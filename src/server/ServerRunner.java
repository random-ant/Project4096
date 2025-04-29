package server;

import mayflower.net.Server;
import mayflower.net.ServerGUI;

public class ServerRunner {
   public static void main(String[] args) {
      // JFrame optionFrame = new JFrame();
      // String s = JOptionPane.showInputDialog(optionFrame, "Enter the Port to Listen
      // to", "1234");

      try {
         // int port = Integer.parseInt(s.trim());
         Server server = new GameServer(1234);
         new ServerGUI(server);
         server.start();
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
