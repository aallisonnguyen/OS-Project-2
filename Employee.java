class Employee implements Runnable{	
	
  int frontDeskID;
  static int roomNumber = 0;
  Thread frontDeskThread;
  

// parameterized constructor implementation
  public Employee(int newID){
    frontDeskID = newID;
    System.out.println("Front desk employee " + frontDeskID + " created");	// prints employee is created
    frontDeskThread = new Thread(this);	// create employee thread
    frontDeskThread.start(); // start employee thread 
  }

// run method implementation
  @Override public void run(){
    try{
      while(true){
		  Hotel.guestsWaitingForCheckIn.acquire();	// ensure guests are in waiting to check in
		  Hotel.employeeMutex.acquireUninterruptibly();	// make sure one employee is trying to access queue at a time
		  Guest guest = Hotel.queueForFrontDesk.remove();	// dequeue first guest
		  roomNumber++;
		  guest.roomNumber = roomNumber;	
		  Hotel.employeeMutex.release();	// other employees can access queue now
		  Hotel.frontDeskIDs[guest.guestID] = frontDeskID;	// set id in front desk array for guest to access later
		  Hotel.employeeChecksGuestIn[guest.guestID].release();	// ensure the employee checks in the correct guest
		  employeeRegistersGuestAndAssignsRoom(frontDeskID,guest.guestID,guest.roomNumber);	// print
		  Hotel.frontDeskAvailable .acquire();	// make sure front desk is free
		  Hotel.employees.release();	// signal that an employee is availble to check in a guest
	}
    }
    catch(Exception e){
    	e.printStackTrace();
    }
  }
  
  public void employeeRegistersGuestAndAssignsRoom(int employeeID, int guestID, int roomNumber) {
	  System.out.println("Front desk employee " + employeeID + " registers guest " + guestID +
		      " and assigns room " + roomNumber);
  }
}				