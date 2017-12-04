import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ReceivedMessagesHandler implements Runnable {

	private InputStream server;

	public ReceivedMessagesHandler(InputStream server) {
		this.server = server;
	}

	@Override
	public void run() {
		// receive server messages and print out to screen
		Scanner s = new Scanner(server);
		while (s.hasNextLine()) {
			System.out.println(s.nextLine());
		}
		s.close();
	}
}