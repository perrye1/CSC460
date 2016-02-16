import java.io.*;
import java.net.*;

public class client {

	public static void main(String[] args) {

		Socket toserversocket;

		System.out.println("CLIENT is waiting to connect....");
		try {
			toserversocket = new Socket("localhost", 4030); // new socket connection to the server
			
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //for reading user input from the console
			DataOutputStream out = new DataOutputStream(toserversocket.getOutputStream( )); //output stream to the server
			DataInputStream in = new DataInputStream(toserversocket.getInputStream( )); //input stream from the server
			
			Boolean gameOver = false;
			String board[][] = new String[][]{ //board is a 2d array of strings, starts out filled with spaces
					{" "," "," "},
					{" "," "," "},
					{" "," "," "}
					};
					
			while(gameOver != true){
				
				String serverMove = in.readUTF();
				String clientMove;
				String parts[];
				
				if(serverMove.equals("NONE")){
					
					//the server is indicating that client should move first
					printBoard(board);
					System.out.println("Enter move in the format row-column");
					clientMove = br.readLine();
					
					//parse out the row and column from the text entered
					int row = Integer.parseInt(clientMove.split("-")[0]);
					int col = Integer.parseInt(clientMove.split("-")[1]);
					
					//make sure the user is not entering out of bounds co-ords or trying to overwrite an already played square
					while(((row>2 || row<0) || (col>2 || col<0)) || (!(board[row][col].equals(" ")))){ //short circuit eval prevents a array out of bounds ex here, if the user enters out of bounds co-ords
						
						System.out.println("move invalid!");
						System.out.println("Enter move in the format row-column");
						clientMove = br.readLine();
						row = Integer.parseInt(clientMove.split("-")[0]);
						col = Integer.parseInt(clientMove.split("-")[1]);
						
					}
					
					
					board[row][col] = "O"; //modify the local copy of the board
					out.writeUTF("MOVE "+row+" "+col); //send the move to the server
					
				}else{
					
					parts = serverMove.split(" ");//parse servers move
					
					if(parts.length == 4){
						
						if(parts[3].equals("WIN")){
							
							//the server is confirming that the clients last move resulted in a win
							printBoard(board); //show the board
							System.out.println("You Won");
							gameOver = true; //end the game
							
						}else if(parts[3].equals("LOSS")){
							
							board[Integer.parseInt(parts[1])][Integer.parseInt(parts[2])] = "X"; //modify local copy of board
							printBoard(board); //show the board
							System.out.println("You Lost");
							gameOver = true; //end the game
							
						}else if(parts[3].equals("TIE")){
							
							if(parts[1].equals("0") & parts[1].equals("0")){
								
								if(board[0][0].equals(" ")){
									
									//the servers last move is actually at 0,0, and it results in a tie
									board[Integer.parseInt(parts[1])][Integer.parseInt(parts[2])] = "X"; //modify local copy of board
									printBoard(board); //show the board
									System.out.println("The game ended in a Tie");
									gameOver = true; //end the game
									
								}else{
									
									//the server is confirming that the clients last move resulted in a tie
									printBoard(board); //show the board
									System.out.println("The game ended in a Tie");
									gameOver = true; //end the game
									
								}
							}else{
								
								board[Integer.parseInt(parts[1])][Integer.parseInt(parts[2])] = "X"; //modify local copy of board
								printBoard(board); //show the board
								System.out.println("The game ended in a Tie");
								gameOver = true; //end the game
							}
						}
					}else{
						
						parts = serverMove.split(" ");//parse servers move
						board[Integer.parseInt(parts[1])][Integer.parseInt(parts[2])] = "X"; //modify local copy of board
						printBoard(board); //show the board
						
						System.out.println("Enter move in the format row-column");
						clientMove = br.readLine();
						
						//parse out the row and column from the text entered
						int row = Integer.parseInt(clientMove.split("-")[0]);
						int col = Integer.parseInt(clientMove.split("-")[1]);
						
						//make sure the user is not entering out of bounds co-ords or trying to overwrite an already played square
						while(((row>2 || row<0) || (col>2 || col<0)) || (!(board[row][col].equals(" ")))){ //short circuit eval prevents a array out of bounds ex here, if the user enters out of bounds co-ords
							
							System.out.println("move invalid!");
							System.out.println("Enter move in the format row-column");
							clientMove = br.readLine();
							row = Integer.parseInt(clientMove.split("-")[0]);
							col = Integer.parseInt(clientMove.split("-")[1]);
							
						}
						
						board[row][col] = "O"; //modify the local copy of the board
						out.writeUTF("MOVE "+row+" "+col); //send the move to the server
						
					}
				}
			}
		} catch (IOException e) {
		}
		;
	}
	
	//prints the board in a pretty tic-tac-toe format
	public static void printBoard(String[][] board){
		System.out.println(" ");
		System.out.println(board[0][0] + " | " + board[0][1] + " | " + board[0][2]);
		System.out.println("---------");
		System.out.println(board[1][0] + " | " + board[1][1] + " | " + board[1][2]);
		System.out.println("---------");
		System.out.println(board[2][0] + " | " + board[2][1] + " | " + board[2][2]);
		System.out.println(" ");
	}
}

