import java.util.Random;

class Guest implements Runnable{
// attributes
  int guestID;
  int roomNumber;
  int numBags;
  
  Hotel h;
  Random rand;
  Thread guestThread;
  
// parameterized constructor implementation
  public Guest(int newID){
    rand = new Random();
    guestID = newID;
    numBags = rand.nextInt(6);	// range 0-5
    System.out.println("Guest " + guestID + " created");
    guestThread = new Thread(this);
    Hotel.hotelGuestThreads[guestID] = guestThread; // so that the guest threads can join in main at the end
    guestThread.start();
  }

  // run method implementation
   @Override public void run(){
    try{
      guestEntersHotel(guestID, numBags);
      Hotel.guestMutex.acquire();	// ensure only one guest tries to join queue at a time
      Hotel.queueForFrontDesk.add(this);	// add to queue
      Hotel.guestMutex.release();	// other guests can now join queue
      Hotel.employees.acquire();	// wait for an employee to be free
      Hotel.guestsWaitingForCheckIn.release();	// signal that there is a guest that is waiting to check in 
      Hotel.employeeChecksGuestIn[guestID].acquire();	// wait for the employee to check in the correct guest
      guestReceivesKey(guestID, roomNumber,
			Hotel.frontDeskIDs[guestID]);
      Hotel.frontDeskAvailable .release();

      // if numBags requires help from bellhop
      if(numBags > 2){
		  Hotel.bellhops.acquire();	// wait for bellhop
		  guestRequestsBagHelp(guestID);
		  Hotel.queueForBellhop.add(this);	// queue for the bellhop
		  Hotel.requestForBellhop.release();	// signal guest needs bellhop
		  Hotel.bagsReceived.acquire();	// wait for the bellhop to receive the bags
		  guestEntersRoom(guestID, roomNumber);
		  Hotel.guestEnteredRoom[guestID].release();	// signal that guest has entered room
		  Hotel.bagsDelivered.acquire();	// wait for bellhop to deliver bags to reoom
		  guestReceivesBagsAndGivesTip(guestID, Hotel.bellhopIDs[guestID]);	// print and tip
      }
      // if numBags is less than 2
      else{
		  Hotel.guestEnteredRoom[guestID].release();	// signal that guest has entered room
		  guestEntersRoom(guestID, roomNumber);
      }
      guestRetires(guestID);	// print guest retires
      guestJoins(guestID);	// increment guest joins for main
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }

  public void guestEntersHotel(int guestID, int numBags){
    System.out.println("Guest " + guestID + " enters the hotel with " +
			numBags + " bags");
  }

  public void guestReceivesKey(int guestID, int roomNumber, int employeeID){
    System.out.println("Guest " + guestID + " receives room key for room " +
			roomNumber + " from front desk employee " +
			employeeID);
  }

  public void guestRequestsBagHelp(int guestID){
    System.out.println("Guest " + guestID + " requests help with bags");
  }

  public void guestEntersRoom(int guestID, int roomNumber){
    System.out.println("Guest " + guestID + " enters room " + roomNumber);
  }

  public void guestReceivesBagsAndGivesTip(int guestID, int bellhopID){
    System.out.println("Guest " + guestID + " receives bags from bellhop " +
			bellhopID + " and gives tip");
  }
  

  public void guestRetires(int guestID){
    System.out.println("Guest " + guestID + " retires for the evening");
  }

 
  public void guestJoins(int guestID){
    Hotel.joinedGuests = Hotel.joinedGuests + 1;
  }

}