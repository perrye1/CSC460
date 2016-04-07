import java.util.Random;

public class Driver {
	
	private static Resource myResource = new Resource();

	public static void main(String[] args) {
		
		int randomNum;
		Random rand = new Random();
		
		//50 times, randomly create reader and writer threads that execute for a random amount of time each
		for (int i=0;i<50;i++){
			randomNum = rand.nextInt(10);
			if(randomNum % 2 == 0){
				WriterThread write = new WriterThread(myResource, randomNum);
				Thread thread = new Thread(write);
				thread.start();
			}else{
				ReaderThread read = new ReaderThread(myResource, randomNum);
				Thread thread = new Thread(read);
				thread.start();
			}
		}

	}
	
}