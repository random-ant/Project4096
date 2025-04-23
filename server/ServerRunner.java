import javax.swing.JFrame;
import javax.swing.JOptionPane;
import mayflower.net.Server;
import mayflower.net.ServerGUI;

/**
 * The entry point for starting the game server.
 * This class initializes the server and its GUI, and starts listening for
 * client connections.
 */
public class ServerRunner {

   /**
    * The main method that starts the game server.
    * 
    * @param args Command-line arguments (not used).
    */
   public static void main(String[] args) {
      // JFrame optionFrame = new JFrame();
      // String s = JOptionPane.showInputDialog(optionFrame, "Enter the Port to Listen
      // to", "1234");

      try {
         // int port = Integer.parseInt(s.trim());
         Server server = new GameServer(1234);

         // Initialize the server GUI
         new ServerGUI(server);

         // Start the server
         server.start();
      } catch (Exception var5) {
         var5.printStackTrace();
      }
   }
}
