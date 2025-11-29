import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Semaphore;

 class Hotel implements Runnable{
	
    final static int MAX_GUESTS = 25;

    // mutexes
    static Semaphore guestMutex = new Semaphore(1);
    static Semaphore employeeMutex = new Semaphore(1);
    static Semaphore bellhopMutex = new Semaphore(1);
    
    // 2 employees and 2 bellhops
    static Semaphore employees = new Semaphore(2);
    static Semaphore bellhops = new Semaphore(2);
  
    // check in process semaphores
    static Semaphore guestsWaitingForCheckIn = new Semaphore(0);
    static Semaphore frontDeskAvailable  = new Semaphore(0);
    static Semaphore employeeChecksGuestIn[] = new Semaphore[MAX_GUESTS];
    
    // bellhop process semaphores
    static Semaphore requestForBellhop = new Semaphore(0);
    static Semaphore bagsReceived = new Semaphore(0);
    static Semaphore bagsDelivered = new Semaphore(0);
    static Semaphore guestEnteredRoom[] = new Semaphore[MAX_GUESTS];
    
    // arrays to hold ids for all 25 guests
    static int frontDeskIDs[] = new int[MAX_GUESTS];
    static int bellhopIDs[] = new int[MAX_GUESTS];

    // queues for guests to join
    static Queue<Guest> queueForFrontDesk = new ArrayDeque<Guest>();
    static Queue<Guest> queueForBellhop = new ArrayDeque<Guest>();
    
    // guest thread array for joined arrays
    static Thread hotelGuestThreads[] = new Thread[MAX_GUESTS];
    private Thread hotelThread;
    static int joinedGuests = 0; 
    
    // default constructor implementation
    public Hotel(){
        hotelThread = new Thread(this);
        hotelThread.start();
    }

    // run method implementation
    @Override
    public void run(){
    	for(int i = 0; i < MAX_GUESTS; i++){
            frontDeskIDs[i] = 0;
            bellhopIDs[i] = 0;
            employeeChecksGuestIn[i] = new Semaphore (0, true);
            guestEnteredRoom[i] = new Semaphore (0, true); 
        }
    }
  
}




