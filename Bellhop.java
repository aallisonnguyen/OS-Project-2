class Bellhop implements Runnable{

  int bellhopID;
  Thread bellhopThread;


  public Bellhop(int newID){
    bellhopID = newID;
    System.out.println("Bellhop " + bellhopID + " created");
    bellhopThread = new Thread(this);
    bellhopThread.start();	
  }

// run method implementation
  @Override public void run(){
    try{
      while(true){
	  Hotel.requestForBellhop.acquire();	// wait for bellhop to be called
	  Hotel.bellhopMutex.acquireUninterruptibly();	// only one bellhop can access queue
	  Guest guest = Hotel.queueForBellhop.remove();	// dequeue from queue
	  Hotel.bellhopIDs[guest.guestID] = bellhopID;	// set bellhopID array for guest to access
	  Hotel.bellhopMutex.release();	// other bellhops can access queue now
	  bellhopReceivesBags(bellhopID, guest.guestID);
	  Hotel.bagsReceived.release();	// signal that the bellhop got the bags from the guest
	  Hotel.guestEnteredRoom[guest.guestID].acquire();	// make sure the correct guest enters their room
	  bellhopDeliversBags(bellhopID, guest.guestID);
	  Hotel.bagsDelivered.release();	// signal bags have been delivered to guest room
	  Hotel.bellhops.release();	// signal that a bellhop is now free
      }
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }
  
  public void bellhopReceivesBags(int bellhopID, int guestID) {
	  System.out.println("Bellhop " + bellhopID +
		      " receives bags from guest " + guestID);
  }
  
  public void bellhopDeliversBags(int bellhopID, int guestID) {
	  System.out.println("Bellhop " + bellhopID +
		      " delivers bags to guest " + guestID);
  }
}