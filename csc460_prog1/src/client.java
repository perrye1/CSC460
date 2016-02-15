import java.io.*;
import java.net.*;

public class client{
		
	public static void main( String[] args){		
		
	  Socket toserversocket;
		
	  System.out.println("CLIENT is waiting to connect....");
	  try {
		toserversocket = new Socket("localhost", 4030);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		DataOutputStream out = new DataOutputStream(toserversocket.getOutputStream( ));
		DataInputStream in = new DataInputStream(toserversocket.getInputStream( ));
		
		Boolean gameOver = false;
		String board[][] = new String[][]{
				{" "," "," "},
				{" "," "," "},
				{" "," "," "}
				};
				
		while(gameOver != true){
			
			String serverMove = in.readUTF();
			String clientMove;
			String parts[];
			
			if(serverMove.equals("NONE")){
				
				printBoard(board);
				System.out.println("Enter move in the format row-column");
				clientMove = br.readLine();
				
				while(!(board[Integer.parseInt(clientMove.split("-")[0])][Integer.parseInt(clientMove.split("-")[1])].equals(" "))){
					
					System.out.println("move invalid!");
					System.out.println("Enter move in the format row-column");
					clientMove = br.readLine();
					
				}
				
				board[Integer.parseInt(clientMove.split("-")[0])][Integer.parseInt(clientMove.split("-")[1])] = "O";
				
				out.writeUTF("MOVE "+Integer.parseInt(clientMove.split("-")[0])+" "+Integer.parseInt(clientMove.split("-")[1]));
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
					
					while(!(board[Integer.parseInt(clientMove.split("-")[0])][Integer.parseInt(clientMove.split("-")[1])].equals(" "))){
						
						System.out.println("move invalid!");
						System.out.println("Enter move in the format row-column");
						clientMove = br.readLine();
						
					}
					
					board[Integer.parseInt(clientMove.split("-")[0])][Integer.parseInt(clientMove.split("-")[1])] = "O";
					
					out.writeUTF("MOVE "+Integer.parseInt(clientMove.split("-")[0])+" "+Integer.parseInt(clientMove.split("-")[1]));
					
				}
			}
			
		}
	  }
 	  catch (IOException  e) {   };
   }
	
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

