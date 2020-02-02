import java.io.*;
import java.util.*;

public class Request {
	
	private String time;
	private int startFloor;
	private String direction;
	private int destFloor;
	
	public Request (String time, int startFloor, String direction, int destFloor) {
		this.time = time;
		this.startFloor = startFloor;
		this.direction = direction;
		this.destFloor = destFloor;
	}
	
	public String getTime () {
		return this.time;
	}
	
	public int getStartFloor () {
		return this.startFloor;
	}
	
	public String getDirection () {
		return this.direction;
	}
	
	public int getDestFloor () {
		return this.destFloor;
	}
	
	public static void main (String[] args) throws FileNotFoundException {
		
		File file = new File("./elevator.txt");
		Scanner scan = new Scanner(file);
		
		List<Request> listOfRequests = new ArrayList<Request>();
		
		while (scan.hasNextLine()) {
			
			String line = scan.nextLine();
			String[] info = line.split(" ");
			
			if (info.length == 4) {
				String time = info[0];
				
				if (time.length() != 10) {
					System.out.println("The time is not in the correct format, cannot process request.");
					continue;
				}
				
				int fNumber = Integer.parseInt(info[1]);
				String dir = info[2];
				int dNumber = Integer.parseInt(info[3]);
				dir.toUpperCase(); //making the direction string upper case so its standard throughout the system
				Request r = new Request(time, fNumber, dir, dNumber);
				listOfRequests.add(r);
				
			} else {
				System.out.print("Did not receive the correct inputs, cannot process request.");
				continue;
			}
		}
		
		 for(Request r: listOfRequests){
			 System.out.println(r.startFloor);
             System.out.println(r.destFloor);
         }
	}
}
