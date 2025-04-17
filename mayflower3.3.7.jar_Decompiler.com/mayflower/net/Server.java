package mayflower.net;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import mayflower.util.Logger;

public abstract class Server extends Thread {
   private int port;
   private boolean running;
   private Map<Integer, ServerSideClient> clients;
   private int nextClientId;
   private Socket clientSocket;
   private List<Logger> loggers;

   public Server(int port) {
      this(port, false);
   }

   public Server(int port, boolean delayStart) {
      this.port = port;
      this.clients = new HashMap();
      this.loggers = new ArrayList();
      this.nextClientId = 1;
      if (!delayStart) {
         this.start();
      }

   }

   public void run() {
      this.running = true;
      ServerSocket serverSocket = null;

      try {
         serverSocket = new ServerSocket(this.port);
      } catch (Exception var6) {
         this.log(var6.getMessage());
         this.running = false;
      }

      while(this.running) {
         try {
            this.clientSocket = serverSocket.accept();
            ServerSideClient client = new ServerSideClient(this.getNextClientId(), this.clientSocket, this);
            this.clients.put(client.getClientId(), client);
            client.start();
            this.onJoin(client.getClientId());
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      }

      try {
         if (this.clientSocket != null) {
            this.clientSocket.close();
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public void shutdown() {
      this.running = false;

      try {
         if (this.clientSocket != null) {
            this.clientSocket.close();
         }

         this.disconnectAll();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public int getPort() {
      return this.port;
   }

   public String getIP() {
      try {
         return InetAddress.getLocalHost().getHostAddress();
      } catch (UnknownHostException var2) {
         var2.printStackTrace();
         return "???.???.???.???";
      }
   }

   public void send(int id, String message) {
      ServerSideClient client = (ServerSideClient)this.clients.get(id);
      if (client != null) {
         client.send(message);
      }

   }

   public void send(String message) {
      ServerSideClient[] clients = (ServerSideClient[])this.clients.values().toArray(new ServerSideClient[0]);
      ServerSideClient[] var6 = clients;
      int var5 = clients.length;

      for(int var4 = 0; var4 < var5; ++var4) {
         ServerSideClient client = var6[var4];
         this.send(client.getClientId(), message);
      }

   }

   public void disconnect(int id) {
      ServerSideClient client = (ServerSideClient)this.clients.remove(id);
      if (client != null) {
         client.send("disconnect");
         client.disconnect();
         this.onExit(id);
      }

   }

   public void disconnectAll() {
      ServerSideClient[] clients = (ServerSideClient[])this.clients.values().toArray(new ServerSideClient[0]);
      ServerSideClient[] var5 = clients;
      int var4 = clients.length;

      for(int var3 = 0; var3 < var4; ++var3) {
         ServerSideClient client = var5[var3];
         this.disconnect(client.getClientId());
      }

   }

   public int numConnects() {
      return this.clients.size();
   }

   public ServerSideClient getClient(int id) {
      return (ServerSideClient)this.clients.get(id);
   }

   public void log(String message) {
      System.out.println(message);
      Iterator var3 = this.loggers.iterator();

      while(var3.hasNext()) {
         Logger logger = (Logger)var3.next();
         logger.log(message);
      }

   }

   public void addLogger(Logger logger) {
      this.loggers.add(logger);
   }

   public abstract void process(int var1, String var2);

   public abstract void onJoin(int var1);

   public abstract void onExit(int var1);

   private int getNextClientId() {
      return this.nextClientId++;
   }
}
