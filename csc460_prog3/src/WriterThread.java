public class WriterThread implements Runnable {
	
	Resource myResource;
	int numTimesToExecute;
	
	public WriterThread (Resource r, int i) {
		myResource = r;
		numTimesToExecute = i;
	}
	
	public void run (){
		
		for (int j=0;j<numTimesToExecute;j++) {
			try {
				myResource.wmutex.acquire();
				myResource.writecount ++;
				System.out.println("writer starting up, writecount is:" + myResource.writecount);
				myResource.wmutex.release();
				if (myResource.writecount > 1 || myResource.readcount > 0){
					myResource.wantin.acquire();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//write to the resource
			System.out.println("writer has entered the resource");
			System.out.println("writing to the resource now");
			
			try {
				myResource.wmutex.acquire();
				myResource.writecount --;
				System.out.println("writer shutting down, writecount is:" + myResource.writecount);
				if(myResource.writecount > 0){
					myResource.wantin.release();
				}else{
					myResource.rmutex.acquire();
					//we have to make this local var because decrementing the actual readcount here results in it being decremented twice overall, which screws the whole thing up
					int localReadCount = myResource.readcount; 
					while (localReadCount > 0){
						myResource.writerswaiting.release();
						localReadCount --;
					}
				}
				myResource.wmutex.release();
				myResource.rmutex.release();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("writer thread has finished");
	}
	
}