package server;

import mayflower.net.*;
import java.util.*;

import game.BColor;
import game.Game;

/**
 * The server the game is hosted on.
 */
public class GameServer extends Server {
	private Queue<Integer> clientsWaitingForGame;
	private Map<Integer, Game> games;
	private Map<Integer, Integer> otherPlayer;
	private Set<Integer> blueClients, redClients;

	public GameServer(int port) {
		super(port, true);

		clientsWaitingForGame = new LinkedList<Integer>();
		games = new HashMap<Integer, Game>();
		otherPlayer = new HashMap<Integer, Integer>();
		blueClients = new HashSet<Integer>();
		redClients = new HashSet<Integer>();

		System.out.println("Waiting for clients on port " + getPort() + " at " + getIP());
	}

	/*
	 * Do something with a message sent from a client.
	 *
	 * Allowed Messages:
	 * addblock row col val
	 * move UP/DOWN/LEFT/RIGHT
	 * render
	 * ready
	 */
	public void process(int id, String message) {
		System.out.println("Message from client " + id + ": " + message);
		// get this client's game
		Game game = games.get(id);

		String[] parts = message.trim().split(" ");
		// check if client is in a game and if that game is not over
		if (game != null || "ready".equals(parts[0])) {
			try {
				if ("addblock".equals(parts[0])) {
					int row = Integer.parseInt(parts[1]);
					int col = Integer.parseInt(parts[2]);
					int val = Integer.parseInt(parts[3]);

					// check if a piece can be placed at (row, col)
					if (null == game.getBlock(row, col)) {
						game.addBlock(row, col, val, BColor.NEUTRAL);
						String response = "addblock " + row + " " + col + " " + val;
						send(otherPlayer.get(id), response);
					} else {
						send(id, "error location is occupied: [" + message + "]");
					}
				} else if ("move".equals(parts[0])) {
					String dir = parts[1];

					String response = "move " + dir;
					send(otherPlayer.get(id), response);
				} else if ("render".equals(parts[0])) {
					send(otherPlayer.get(id), "render");
				} else if ("ready".equals(parts[0])) {
					System.out.println("" + id + " ready");
					if (!clientsWaitingForGame.contains(id))
						clientsWaitingForGame.add(id);
					startNew();
				} else {
					send(id, "error game not found");
				}
			} catch (Exception e) {
				send(id, "error invalid request: [" + message + "]");
				e.printStackTrace();
			}
		} else {
			send(id, "error, game doesn't exist??");
			System.out.println(parts);
		}
	}

	/*
	 * Do something when a client connects.
	 *
	 * For every 2nd client that connects:
	 * 1. Create a new TicTacToe game for these clients
	 * 2. Randomly assign each client X or O
	 * 3. Assign clientId -> TicTacToe game in clientGames map
	 */
	public void onJoin(int id) {
		// add new client to the game queue
		System.out.println("Client connected: " + id);
		send(id, "join");
	}

	/**
	 * Start the game. Connect both clients and render the new grid.
	 */
	public void startNew() {
		System.out.println("start new");
		// If there are at least 2 clients waiting for a game...
		if (clientsWaitingForGame.size() >= 2) {
			System.out.println("two ready");
			// get the two clients that have been waiting the longest
			Integer clientA = clientsWaitingForGame.remove();
			Integer clientB = clientsWaitingForGame.remove();

			// create a mapping between each of they players so that you can always find the
			// *other* player
			otherPlayer.put(clientA, clientB);
			otherPlayer.put(clientB, clientA);

			// create TicTacToe game and mapping from clients -> game
			Game game = new Game();
			games.put(clientA, game);
			games.put(clientB, game);

			// flip a coin to pick which client is BLUE and which is RED
			// store each client in the appropriate Set (blueClients, redClients)
			// send message to each client informing them of their color (BLUE, RED)
			// This message also lets the client's know that their game is ready to be
			// played
			int r = (int) (Math.random() * 2);
			if (r == 0) {
				blueClients.add(clientA);
				redClients.add(clientB);

				send(clientA, "youare BLUE");
				send(clientB, "youare RED");
			} else {
				blueClients.add(clientB);
				redClients.add(clientA);

				send(clientB, "youare BLUE");
				send(clientA, "youare RED");
			}

			send(clientA, "render");
			send(clientB, "render");

		}
	}

	/*
	 * Do something when a client disconnects
	 *
	 * End the game, make the other player the winner!
	 */
	public void onExit(int id) {
		System.out.println("Client disconnected: " + id);
		// check if this client is waiting in the queue
		if (clientsWaitingForGame.contains(id)) {
			clientsWaitingForGame.remove(id);
		}

		// check if this player is already in a game
		Game game = games.get(id);
		if (game != null) {
			// tell the other player that they win!
			send(otherPlayer.get(id), "winner disconnect");

			endGame(id, otherPlayer.get(id));
		}
	}

	/**
	 * End the game.
	 * 
	 * @param clientA ID of client A
	 * @param clientB ID of client B
	 */
	private void endGame(int clientA, int clientB) {
		// disconnect both clients
		disconnect(clientA);
		disconnect(clientB);

		// remove the data associated with these clients from data structures:
		// otherPlayer, games, xClients, oClients
		otherPlayer.remove(clientA);
		otherPlayer.remove(clientB);

		games.remove(clientA);
		games.remove(clientB);

		blueClients.remove(clientA);
		blueClients.remove(clientB);

		redClients.remove(clientA);
		redClients.remove(clientB);
	}
}