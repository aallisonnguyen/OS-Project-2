public class Project2 {
	  public static void main(String[]args){
	        System.out.println("Simulation starts");

	        Hotel hotel = new Hotel();

	        // create 2 employees
	        for(int i = 0; i < 2; i++){
	            new Employee(i);
	        }

	        // create 2 bellhops
	        for(int i = 0; i < 2; i++){
	            new Bellhop(i);
	        }

	        // create 25 guests
	        for(int i = 0; i < Hotel.MAX_GUESTS; i++){
	            new Guest(i);
	        }

	        // used for guests to join once they are done with their tasks
	        for(int i = 0; i < Hotel.MAX_GUESTS; i++){
	            try {
	                Hotel.hotelGuestThreads[i].join();
	                System.out.println("Guest " + i + " joined");
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	        
	        // prints simulation ends when all 25 guests have joined
	        if(Hotel.joinedGuests == Hotel.MAX_GUESTS) {
	        	System.out.println("Simulation ends");
	        }
	        
	        System.exit(0);
	    }
}
