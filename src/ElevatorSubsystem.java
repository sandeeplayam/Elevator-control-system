import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Elevator sub system class for Sysc 3303 Iteration 1 group 2
 * 
 * @author Sudarsana Sandeep 100963087
 *
 */
public class ElevatorSubsystem extends Thread {

	private static int preDelay = 2000;
	private static int postDelay = 3000;
	static private int timeMotor = 3000;
	private ArrayList<Boolean> listOfButtons;
	private ArrayList<Boolean> listOfLamps;
	private int currFloor;
	private int destinationFloor;
	private Boolean goUp;
	private Boolean goDown;
	private Boolean motorOn = false;
	private Boolean doorIsClosed;
	private Scheduler scheduler;
	private Request request;
	private HashMap<Integer, Request> requestList;
	State state = State.IDLE;
	private String eleName;
	// public ArrayList<Request> requestList;

	public ElevatorSubsystem(String name, Scheduler scheduler) {

		this.eleName = name;
		doorIsClosed = true; // initially setting the door of the elevator to closed

		this.currFloor = 1;
		this.scheduler = scheduler;

		listOfButtons = new ArrayList<Boolean>(Arrays.asList(new Boolean[22])); // instantiating the list of buttons
		Collections.fill(listOfButtons, Boolean.FALSE); // filling the list with false boolean values

		listOfLamps = new ArrayList<Boolean>(Arrays.asList(new Boolean[22])); // instantiating the list of lamps
		Collections.fill(listOfLamps, Boolean.FALSE); // filling the list with false boolean values

		requestList = new HashMap<Integer, Request>();
	}

	public void run() {
		for (;;) {

			Map.Entry<Integer, Request> entry = this.scheduler.executeRequest();
			this.requestList.put(entry.getKey(), entry.getValue());
	
			if (!requestList.isEmpty()) {
				elevatorState(entry);

			} else {
				System.out.println(getEleName() + "ready, waiting for requests from the Scheduler in an idle state.");
			}
			
			System.out.println(getEleName() + ": " + entry.getValue());
			System.out.println("----------------------------------------------------------------");
			this.scheduler.runCompleted();

		}
	}

	public enum State {
		IDLE, RUN, ARRIVING;
	}

	public synchronized void elevatorState(Map.Entry<Integer, Request> entry) {

		request = entry.getValue();
		boolean finished = false;

		while (!finished) {

			switch (state) {

			case IDLE:

				System.out.println("Elevator has received a request from the scheduler, processing request.");
				if (this.currFloor == request.getStartFloor()) {

					try {
						doorOpen();
						Thread.sleep(ElevatorSubsystem.preDelay);
					} catch (InterruptedException e) {
						System.out.println("There is an error in opening the door \n");
						e.printStackTrace();
					}

					try {
						doorClose();
						Thread.sleep(ElevatorSubsystem.preDelay);
					} catch (InterruptedException e) {
						System.out.println("There is an error in closing the door \n");
						e.printStackTrace();
					}
					state = State.RUN;
				}

				if (this.currFloor != request.getStartFloor()) {
					state = State.RUN;
				}

				break;

			case RUN:
				int diff = this.currFloor - request.getStartFloor();
				int directionIndex = 1;

				if(diff > 0) {
					directionIndex=-1;
				}
				diff = Math.abs(diff);
				
				if (this.currFloor == request.getStartFloor()) {
					userDestination(request.getDestFloor());
					
					diff = this.currFloor - request.getDestFloor();

					
					if(diff > 0) {
						directionIndex = -1;
					}
					
					diff = Math.abs(diff);
					
				}

				try {
					triggerMotor();
					triggerElevator(entry);
					
					while (diff != 0) {
						Thread.sleep(ElevatorSubsystem.timeMotor);
						
						
						this.currFloor+=directionIndex;

						diff--;
					}
				} catch (InterruptedException e) {
					System.out.println("Error running the motor \n");
					e.printStackTrace();
				}
				
				state = State.ARRIVING;
				break;

			case ARRIVING:

				sendArrivalInfo(entry);
				triggerMotor();

				try {
					doorOpen();
					Thread.sleep(ElevatorSubsystem.preDelay);
				} catch (InterruptedException e) {
					System.out.println("There is an error in opening the door \n");
					e.printStackTrace();
				}

				try {
					doorClose();
					Thread.sleep(ElevatorSubsystem.preDelay);
				} catch (InterruptedException e) {
					System.out.println("There is an error in closing the door \n");
					e.printStackTrace();
				}

				if (this.currFloor == request.getDestFloor()) {
					requestList.remove(entry.getKey()); 
					finished = true;
					state = State.IDLE;
				} else {
					state = State.RUN;
				}

				break;
			}

		}

	}

	public void triggerElevator(Map.Entry<Integer, Request> entry) {

		request = entry.getValue();

		if (this.currFloor == request.getStartFloor()) {
			if (request.getDirection() == "UP") {
				System.out.println(getEleName() + " going up to floor " + request.getDestFloor()
						+ " to drop off passenger.");
			} else {
				System.out.println(getEleName() + " going down to floor " + request.getDestFloor()
						+ " to drop off passenger.");
			}
		} else if (this.currFloor != request.getStartFloor()) {
			if (this.currFloor < request.getStartFloor()) {
				System.out.println(getEleName() + " going up to floor " + request.getStartFloor()
						+ " to pick up passenger.");
			} else {
				System.out.println(getEleName() + " going down to floor " + request.getStartFloor()
						+ " to pick up passenger.");
			}
		}
	}

	public void triggerMotor() {
		if (!motorOn) {

			motorOn = true;
			System.out.println("Motor is running.");

		} else if (motorOn) {

			motorOn = false;
			System.out.println("Motor is off.");
		}
	}

	public void sendArrivalInfo(Map.Entry<Integer, Request> entry) {
		request = entry.getValue();

		if (this.currFloor == request.getStartFloor()) {
			System.out.println(getEleName() + " has arrived at floor " + request.getStartFloor() + ".");
		} else if (this.currFloor == request.getDestFloor()) {
			System.out.println(getEleName() + " has arrived at floor " + request.getDestFloor() + ".");
		}
	}

	/*
	 * public void addRequest (Integer key, Request value) { requestList.put(key,
	 * value); }
	 */

	public ArrayList<Boolean> getListOfButtons() {
		return listOfButtons;
	}

	public ArrayList<Boolean> getListOfLamps() {
		return listOfLamps;
	}

	public void setListOfButtons(ArrayList<Boolean> listOfButtons) {
		this.listOfButtons = listOfButtons;
	}

	public void setListOfLamps(ArrayList<Boolean> listOfLamps) {
		this.listOfLamps = listOfLamps;
	}

	public String getEleName() {
		return this.eleName;
	}

	public int getCurrFloor() {
		return currFloor;
	}

	public void setDirectionUp(Boolean goUp) {
		this.goUp = goUp;
	}

	public void setDirectionDown(Boolean goDown) {
		this.goDown = goDown;
	}

	public Boolean getDirectionUp() {
		return this.goUp;
	}

	public Boolean getDirectionDown() {
		return this.goDown;
	}

	public void setDoorClosed(Boolean closed) {
		this.doorIsClosed = closed;
	}

	/*
	 * public Boolean isOn () { return this.functioning; }
	 */

	public void doorClose() {
		setDoorClosed(true);
		System.out.println("The Elevator is closing doors \n");
	}

	public void doorOpen() {
		setDoorClosed(false);
		System.out.println("The Elevator is opening doors \n");
	}

	public int getDestinationFloor() {
		return this.destinationFloor;
	}

	public void setDestinationFloor(int floor) {
		this.destinationFloor = floor;
	}

	public void userDestination(int floor) {
		setDestinationFloor(floor);
		getListOfButtons().set(floor, true);
		getListOfLamps().set(floor, true);
	}

	public void reachedDestination(int floor) {
		getListOfButtons().set(floor, false);
		getListOfLamps().set(floor, false);
		try {
			doorOpen();
			Thread.sleep(preDelay);

		} catch (InterruptedException e) {
			System.out.println("There is an error opening the door \n");
			e.printStackTrace();
		}

		try {
			doorClose();
			Thread.sleep(postDelay);

		} catch (InterruptedException e) {
			System.out.println("There is an error closing the door \n");
			e.printStackTrace();
		}
	}
	
	public Scheduler getScheduler() {
		return this.scheduler;
	}
	
	public boolean getMotorOn() {
		return this.motorOn;
	}
	
	public boolean getDoorClosed() {
		return this.doorIsClosed;
	}

}
}
