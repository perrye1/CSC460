import java.net.*;
import java.io.*; 
import java.util.Random;

public class TicTacToeThread implements Runnable {
	
	private Socket toclientsocket;
	private int ID;
	
	public TicTacToeThread(Socket s, int i) {
		this.toclientsocket = s;
		this.ID = i;
	}
	
	public void run()
	{				
		try { 
			
			DataOutputStream out = new DataOutputStream(toclientsocket.getOutputStream( )); //output stream to the client
			DataInputStream in = new DataInputStream(toclientsocket.getInputStream()); //input stream from the client
			
			int moveCount = 0; //keeps track of the number of moves made by both client and server (9 possible)
			boolean gameOver = false;
			boolean serverMoveFirst;
			Random rand = new Random();
			
			String board[][] = new String[][]{ //board is a 2d array of strings, starts out filled with spaces
					{" "," "," "},
					{" "," "," "},
					{" "," "," "}
					};
			
			String clientMove;
			String serverMove;
			String parts[];
			
			//randomly deciding if the client or the server moves first
			if(rand.nextInt(2) == 1) {
			    serverMoveFirst = true;
			}else{
				serverMoveFirst = false;
			}
			
			if(serverMoveFirst){
				
				//generate a move and send to client
				serverMove = generateMove(board);
				out.writeUTF(serverMove);
				System.out.println("server sent: "+serverMove+" to client "+ID);
				moveCount++;
				
			}else{
				
				//tell the client to move first
				out.writeUTF("NONE");
				System.out.println("server sent: NONE to client "+ID);
				
			}
			
			while(!gameOver==true){
			
				clientMove = in.readUTF(); //read in the clients move from the in stream
				System.out.println("client "+ID+" sent: "+clientMove);
				parts = clientMove.split(" ");//parse clients move
				
				board[Integer.parseInt(parts[1])][Integer.parseInt(parts[2])] = "O";
				moveCount++; //increment the move count
				
				if(checkWin(board).equals("O")){
					
					//if the client has won, don't make any more moves and send the win message
					out.writeUTF("MOVE 0 0 WIN");
					System.out.println("server sent: MOVE 0 0 WIN to client "+ID);
					gameOver = true;
					
				}else if(moveCount==9){
					
					//if there is no winner and the move count is 9, it must be a tie, so send the tie message
					out.writeUTF("MOVE 0 0 TIE");
					System.out.println("server sent: MOVE 0 0 TIE to client "+ID);
					gameOver = true;
					
				}else{
					
					//if there is no winner and move count is not 9, then we are still playing and it is the servers turn
					serverMove = generateMove(board);
					moveCount++;
					
					if(checkWin(board).equals("X")){
						
						//once the server has moved, if it has won, send the client the loss message
						out.writeUTF(serverMove+" LOSS");
						System.out.println("server sent: "+serverMove+" LOSS to client "+ID);
						gameOver = true;
						
					}else if(moveCount==9){
						
						//once the server has moved, if it is the 9th move, send the client the tie message
						out.writeUTF(serverMove+" TIE");
						System.out.println("server sent: "+serverMove+" TIE to client "+ID);
						gameOver = true;
						
					}else{
						
						//once the server has moved, and it is neither a server win or a tie, send the move to the client. Play continues
						out.writeUTF(serverMove);
						System.out.println("server sent: "+serverMove+" to client "+ID);
						
					}
				}
				
			}			
    		}   // end try
    		catch (IOException e) {}  
	}  // end main
	
	//randomly moves to an empty square on the board
	public static String generateMove(String[][] board){
		
		Random rand = new Random();
		int pos1 = rand.nextInt(3);
		int pos2 = rand.nextInt(3);
		while(!board[pos1][pos2].equals(" ")){
			pos1 = rand.nextInt(3);
			pos2 = rand.nextInt(3);		
		}
		
		board[pos1][pos2] = "X";
		
		return "MOVE "+pos1+" "+pos2;
		
	}
	
	//check if there is a winner by comparing all the cells in each row, column and diagonal
	//flags h,v,diag1,diag2 are set if any of these checks come back true
	//if any of the flags are set, we look where it was set, find the piece there, and this must be the winner
	public static String checkWin(String[][] board){
		for(int i = 0; i < 3; i++){
			 boolean h = board[i][0].equals(board[i][1]) && board[i][0].equals(board[i][2]);
			 boolean v = board[0][i].equals(board[1][i]) && board[0][i].equals(board[2][i]);
			 if(h){
				 return board[i][0];
			 }
			 if(v){
				 return board[0][i];
			 }
		}
		
		boolean diag1 = board[0][0].equals(board[1][1]) && board[0][0].equals(board[2][2]);
		boolean diag2 = board[0][2].equals(board[1][1]) && board[0][2].equals(board[2][0]);
		if(diag1 || diag2){
			 return board[1][1];
		}
		
		return " "; //if no winner, then return a blank space
	}
	
}  // end class
