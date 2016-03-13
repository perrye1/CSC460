import java.net.*;
import java.io.*; 

public class Server {

	private static int port = 4030;

	public static void main(String[] args) {
		int i = 0;

		try {
			ServerSocket listener = new ServerSocket(port);
			Socket client;

			while (true) {
				client = listener.accept();
				TicTacToeThread play = new TicTacToeThread(client, i);
				Thread t = new Thread(play);
				t.start();
				i++;
			}
			
		} catch (IOException ioe) {
			System.out.println("IOException on socket listen: " + ioe);
			ioe.printStackTrace();
		}
		
	}
}
