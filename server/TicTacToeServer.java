import mayflower.net.*;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.Queue;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class TicTacToeServer extends Server {
	private Queue<Integer> clientsWaitingForGame;
	private Map<Integer, Game> games;
	private Map<Integer, Integer> otherPlayer;
	private Set<Integer> blueClients, redClients;

	public TicTacToeServer(int port) {
		super(port);

		clientsWaitingForGame = new LinkedList<Integer>();
		games = new HashMap<Integer, Game>();
		otherPlayer = new HashMap<Integer, Integer>();
		blueClients = new HashSet<Integer>();
		redClients = new HashSet<Integer>();

		System.out.println("Waiting for clients on port " + getPort() + " at " + getIP());
	}

	/*
	 * Do something with a message sent from a client
	 *
	 * Allowed Messages:
	 * play row col
	 */

	// FIX FIX FIX FIX

	public void process(int id, String message) {
		System.out.println("Message from client " + id + ": " + message);
		// get this client's game
		Game game = games.get(id);

		// check if client is in a game and if that game is not over
		if (game != null && !game.isGameOver()) {
			// check if it is this players turn
			BColor player = blueClients.contains(id) ? BColor.BLUE : BColor.RED;
			BColor curr = game.getCurrentPlayer();
			if (curr == player) {
				// parse the row and col from the message
				String[] parts = message.trim().split(" ");
				if (parts.length >= 3) {
					try {
						// Integer.parseInt() can throw an exception
						int row = Integer.parseInt(parts[1]);
						int col = Integer.parseInt(parts[2]);

						// check if a piece can be placed at (row, col)
						if (null == game.getBlock(row, col)) {

							// FIX FIX

							double spawnValue = Math.random();
							int value;
							if (spawnValue <= 0.7) {
								value = 2;
							} else if (spawnValue <= 0.9) {
								value = 4;
							} else {
								value = 8;
							}
							game.addBlock(row, col, value, BColor.NEUTRAL);
							game.nextPlayer();

							// broadcast this move to both clients in this game
							String response = "addpiece " + row + " " + col;
							send(id, response);
							send(otherPlayer.get(id), response);

							// check if game is over
							// if it is, remove the game and clients from memory
							if (game.isGameOver()) {
								endGame(id, otherPlayer.get(id));
							}
						} else {
							send(id, "error location is occupied: [" + message + "]");
						}
					} catch (Exception e) {
						send(id, "error invalid request: [" + message + "]");
					}
				} else {
					send(id, "error invalid request: [" + message + "]");
				}
			} else {
				send(id, "error not your turn");
			}
		} else {
			send(id, "error game not found");
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
		clientsWaitingForGame.add(id);
		System.out.println("Client connected: " + id);

		// If there are at least 2 clients waiting for a game...
		if (clientsWaitingForGame.size() >= 2) {
			// get the two clients that have been waiting the longest
			int clientA = clientsWaitingForGame.remove();
			int clientB = clientsWaitingForGame.remove();

			// create a mapping between each of they players so that you can always find the
			// *other* player
			otherPlayer.put(clientA, clientB);
			otherPlayer.put(clientB, clientA);

			// create TicTacToe game and mapping from clients -> game
			TicTacToe game = new TicTacToe();
			games.put(clientA, game);
			games.put(clientB, game);

			// flip a coin to pick which client is X and which is O
			// store each client in the appropriate Set (xClients, oClients)
			// send message to each client informing them of their piece (X, O)
			// This message also lets the client's know that their game is ready to be
			// played
			int r = (int) (Math.random() * 2);
			if (r == 0) {
				xClients.add(clientA);
				oClients.add(clientB);

				send(clientA, "youare X");
				send(clientB, "youare O");
			} else {
				xClients.add(clientB);
				oClients.add(clientA);

				send(clientB, "youare X");
				send(clientA, "youare O");
			}
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
		TicTacToe game = games.get(id);
		if (game != null) {
			// tell the other player that they win!
			send(otherPlayer.get(id), "winner disconnect");

			endGame(id, otherPlayer.get(id));
		}
	}

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

		xClients.remove(clientA);
		xClients.remove(clientB);

		oClients.remove(clientA);
		oClients.remove(clientB);
	}
}