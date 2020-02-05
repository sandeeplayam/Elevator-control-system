
/**
 * @author Group #2
 *
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Request {

	private String time;
	private int startFloor;
	private String direction;
	private int destFloor;

	public Request(String time, int startFloor, String direction, int destFloor) {
		this.time = time;
		this.startFloor = startFloor;
		this.direction = direction;
		this.destFloor = destFloor;
	}

	public String getTime() {
		return this.time;
	}

	public int getStartFloor() {
		return this.startFloor;
	}

	public String getDirection() {
		return this.direction;
	}

	public int getDestFloor() {
		return this.destFloor;
	}

	@Override
	public String toString() {
		return this.time + " " + this.startFloor + " " + this.direction + " " + this.destFloor;
	}

	public static void main(String[] args) throws FileNotFoundException {

		File file = new File("./elevator.txt");
		Scanner scan = new Scanner(file);

		List<Request> listOfRequests = new ArrayList<Request>();

		while (scan.hasNextLine()) {

			String line = scan.nextLine();
			String[] info = line.split(" ");
			EventErrorHandler error = new EventErrorHandler(info);
			// error.strChecker();

			if (error.strChecker() == true) {
				if (info.length == 4) {

					String time = info[0];

					int fNumber = Integer.parseInt(info[1]);

					String dir = info[2];
					dir = dir.toUpperCase(); // making the direction string upper case so its standard throughout the
												// system

					int dNumber = Integer.parseInt(info[3]);
					Request r = new Request(time, fNumber, dir, dNumber);
					listOfRequests.add(r);

				} else {
					System.out.print("Did not receive the correct inputs, cannot process request. \n");
					continue;
				}
			} else {
				continue;
			}
		}
		scan.close();
//		System.out.println(listOfRequests);
		Scheduler scheduler = new Scheduler("scheduler");
		FloorSubsystem floor = new FloorSubsystem("Floor ", listOfRequests, scheduler);
		ElevatorSubsystem elevator = new ElevatorSubsystem("Elevator ", scheduler);
		scheduler.start();
		floor.start();
		elevator.start();
	}
}
