import java.util.concurrent.Semaphore;

public class Resource {
	
	public Semaphore wmutex,rmutex;
	public Semaphore writerswaiting;
	public Semaphore wantin;
	
	public int readcount,writecount;
	
	public Resource() {
		wmutex = new Semaphore(1);
		rmutex = new Semaphore(1);
		writerswaiting = new Semaphore(0);
		wantin = new Semaphore(1);
		readcount = 0;
		writecount = 0;
	}
	
}