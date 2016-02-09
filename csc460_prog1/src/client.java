import java.io.*;
import java.net.*;

public class client
{
		
	public static void main( String[] args)
	{		
		
	  Socket toserversocket;
		
	  System.out.println("CLIENT is attempting connection....");
	  try {
		toserversocket = new Socket("localhost", 4030);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		DataOutputStream outclient = new DataOutputStream(toserversocket.getOutputStream( ));
		
		System.out.println("CONNECTION HAS BEEN MADE");
		
		
		while(true){
			
			System.out.println("Enter String: ");
			String s = br.readLine();
			System.out.println(s);
			outclient.writeChars(s);
			
		}
	  }
 	  catch (IOException  e) {   };
         }
}
