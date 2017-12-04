import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientHandler implements Runnable {

	private Server server;
	private InputStream client;

	public ClientHandler(Server server, InputStream client) {
		this.server = server;
		this.client = client;
	}

	@Override
	public void run() {
		String message;
		
		// when there is a new message, broadcast to all
		Scanner sc = new Scanner(this.client);
		while (sc.hasNextLine()) {
			message = sc.nextLine();
			server.broadcastMessages(message);
		}
		sc.close();
	}
}