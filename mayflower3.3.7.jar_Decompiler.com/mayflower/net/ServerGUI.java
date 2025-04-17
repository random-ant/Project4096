package mayflower.net;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;
import mayflower.util.Logger;

public class ServerGUI implements Logger {
   private JTextArea textArea = new JTextArea();

   public ServerGUI(final Server server) {
      server.addLogger(this);
      JFrame frame = new JFrame("Server");
      frame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {
            server.log("Shutting down...");
            server.shutdown();
         }
      });
      JPanel topPanel = new JPanel();
      topPanel.add(new JLabel("IP Address: " + server.getIP() + " Port: " + server.getPort()));
      frame.add(topPanel, "North");
      JPanel panel = new JPanel();
      panel.setLayout(new BorderLayout());
      this.textArea.setEditable(false);
      DefaultCaret caret = (DefaultCaret)this.textArea.getCaret();
      caret.setUpdatePolicy(2);
      JScrollPane scrollPane = new JScrollPane(this.textArea);
      scrollPane.setVerticalScrollBarPolicy(22);
      panel.add(scrollPane);
      frame.getContentPane().add(panel);
      frame.setDefaultCloseOperation(3);
      frame.setSize(800, 600);
      frame.setResizable(true);
      frame.setVisible(true);
   }

   public void log(Object message) {
      this.textArea.append(message + "\n");
   }
}
