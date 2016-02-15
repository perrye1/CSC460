import java.net.*;
import java.io.*; 
import java.util.Random;

public class server
{

		
	@SuppressWarnings("deprecation")
	public static void main( String[] args)
	{		
		ServerSocket SS; // this is the "door" 
		
		Socket toclientsocket;

		
		try {    // NOTE - must be within a try-clause or throw exceptions!!!!
	  		
		SS = new ServerSocket(4030);   //listen at the door
		while (true){
			System.out.println("waiting for connection");
			toclientsocket = SS.accept();   // block UNTIL request received
			System.out.println("connection made");
			
			DataOutputStream out = new DataOutputStream(toclientsocket.getOutputStream( ));
			DataInputStream in = new DataInputStream(toclientsocket.getInputStream());
			
			int moveCount = 0;
			boolean gameOver = false;
			boolean serverMoveFirst;
			Random rand = new Random();
			
			String board[][] = new String[][]{
					{" "," "," "},
					{" "," "," "},
					{" "," "," "}
					};
			
			String clientMove;
			String serverMove;
			String parts[];
			
			if(rand.nextInt(2) == 1) {
			    serverMoveFirst = true;
			}else{
				serverMoveFirst = false;
			}
			
			if(serverMoveFirst){
				//generate a move and send to client
				serverMove = generateMove(board);
				out.writeUTF(serverMove);
				System.out.println("server sent: "+serverMove);
				moveCount++;
			}else{
				//tell the client to move first
				out.writeUTF("NONE");
				System.out.println("server sent: NONE");
			}
			
			while(!gameOver==true){
			
				clientMove = in.readUTF();
				System.out.println("client sent: "+clientMove);
				parts = clientMove.split(" ");//parse clients move
				
				board[Integer.parseInt(parts[1])][Integer.parseInt(parts[2])] = "O";
				moveCount++;
				if(checkWin(board).equals("O")){
					out.writeUTF("MOVE 0 0 WIN");
					System.out.println("server sent: MOVE 0 0 WIN");
					gameOver = true;
				}else if(moveCount==9){
					out.writeUTF("MOVE 0 0 TIE");
					System.out.println("server sent: MOVE 0 0 TIE");
					gameOver = true;
				}else{
					serverMove = generateMove(board);
					moveCount++;
					if(checkWin(board).equals("X")){
						out.writeUTF(serverMove+" LOSS");
						System.out.println("server sent: "+serverMove+" LOSS");
						gameOver = true;
					}else if(moveCount==9){
						out.writeUTF(serverMove+" TIE");
						System.out.println("server sent: "+serverMove+" TIE");
						gameOver = true;
					}else{
						out.writeUTF(serverMove);
						System.out.println("server sent: "+serverMove);
					}
				}
				
			}			
		}
    		}   // end try
    		catch (IOException e) {}  
	}  // end main
	
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
		
		return " ";
	}
	
}  // end class
