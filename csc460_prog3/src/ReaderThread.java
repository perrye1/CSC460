public class ReaderThread implements Runnable {
	
	Resource myResource;
	int numTimesToExecute;
	
	public ReaderThread (Resource r, int i) {
		myResource = r;
		numTimesToExecute = i;
	}
	
	public void run (){
		
		for (int j=0;j<numTimesToExecute;j++) {
			try {
				myResource.rmutex.acquire();
				myResource.readcount ++;
				System.out.println("reader starting up, readcount is:" + myResource.readcount);
				myResource.rmutex.release();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	
			try {
				myResource.wmutex.acquire();
				if(myResource.writecount > 0){
					myResource.wmutex.release();
					myResource.writerswaiting.acquire();
				}else{
					myResource.wmutex.release();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println("reader has entered the resource");
			//actually do the read operation
			System.out.println("reading from the resource now");
			
			try {
				myResource.rmutex.acquire();
				myResource.wmutex.acquire();
				myResource.readcount --;
				System.out.println("reader shutting down, readcount is:" + myResource.readcount);
				if((myResource.readcount == 0)&&(myResource.writecount > 0)){
					myResource.wantin.release();
					myResource.rmutex.release();
					myResource.wmutex.release();
				}else{
					myResource.rmutex.release();
					myResource.wmutex.release();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
}