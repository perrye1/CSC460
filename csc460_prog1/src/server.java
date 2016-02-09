import java.net.*;
import java.io.*;   


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
			boolean gameOver = false;
			DataInputStream in = new DataInputStream(toclientsocket.getInputStream( ));
			String recieved = "";
			
			System.out.println("connection made");
			
			while(!gameOver==true){
				
				recieved = in.readUTF();
				if(recieved.equals("exit")){
					gameOver=true;
				}else{
					System.out.println(recieved);
				}
				
			}
			 
			
		}
		
    		}   // end try
    		catch (IOException e) {}  
	}  // end main
}  // end myserver class
