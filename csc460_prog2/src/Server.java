import java.net.*;
import java.io.*; 

public class Server {

	private static int port = 4030;

	public static void main(String[] args) {
		int i = 0; //autoID for the clients

		try {
			ServerSocket listener = new ServerSocket(port);
			Socket client;

			while (true) {
				client = listener.accept(); //accept client connection
				TicTacToeThread play = new TicTacToeThread(client, i); //pass client and autoID to new thread
				Thread t = new Thread(play); //create thread object
				t.start(); //start the thread
				i++; //increment the autoID
			}
			
		} catch (IOException ioe) {
			System.out.println("IOException on socket listen: " + ioe);
			ioe.printStackTrace();
		}
		
	}
}
