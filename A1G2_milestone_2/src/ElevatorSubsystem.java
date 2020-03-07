import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**

 * Class represents an elevator subsystem which controls the elevators
 * ElevatorSubsystem is a thread
 * @author Group #2
 */
public class ElevatorSubsystem extends Thread {

	private static int preDelay = 2000;
//	private static int postDelay = 3000;
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
	
	/**
	 * Constructor for the Elevator Subsystem 
	 * @param name The name for the Elevator Subsystem being constructed 
	 * @param scheduler Passing an instance of the scheduler into the elevator subsystem
	 */
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
	
	/**
	 * Since ElevatorSubsystem extends Thread, it requires an override of the run method.
	 * Overridden ElevatorSubsystem run method waits for a request from the scheduler and
	 * starts the state machine. 
	 */
	public void run() {
		for (;;) {

			Map.Entry<Integer, Request> entry = this.scheduler.executeRequest();
			this.requestList.put(entry.getKey(), entry.getValue());

			if (!requestList.isEmpty()) {
				elevatorState(entry);

			} else {
				System.out.println(getEleName() + "ready, waiting for requests from the Scheduler in an idle state.");
			}

			
			System.out.println(getEleName() + " completed request -> " + entry.getValue());
			System.out.println("--------------------------------------End of Request-----------------------------------\n\n");
			this.scheduler.runCompleted();

		}
	}
	
	public enum State {
		IDLE, RUN, ARRIVING;
	}
	
	/**
	 * This method is the FSM of the ElevatorSubsystem 
	 * @param entry Is the current request that is about to be processed by the FSM
	 */
	public void elevatorState(Map.Entry<Integer, Request> entry) {

		request = entry.getValue();
		boolean finished = false;

		while (!finished) {

			switch (state) {

			case IDLE:

				System.out.println("\n *********Elevator IDLE State...********* \n");
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
					
					try {
						Thread.sleep(ElevatorSubsystem.preDelay);
					} catch (Exception e) {
						System.err.println(e);

					}
					
					state = State.RUN;
				}

				break;

			case RUN:
				System.out.println("\n *********Elevator RUN state...********* \n");
				int diff = this.currFloor - request.getStartFloor();
				int directionIndex = 1;

				if (diff > 0) {
					directionIndex = -1;
				}
				diff = Math.abs(diff);

				if (this.currFloor == request.getStartFloor()) {
					userDestination(request.getDestFloor());

					diff = this.currFloor - request.getDestFloor();

					if (diff > 0) {
						directionIndex = -1;
					}

					diff = Math.abs(diff);

				}

				try {
					triggerMotor();
					triggerElevator(entry);

					while (diff != 0) {
						Thread.sleep(ElevatorSubsystem.timeMotor);

						this.currFloor += directionIndex;

						diff--;
					}
				} catch (InterruptedException e) {
					System.out.println("Error running the motor");
					e.printStackTrace();
				}

				state = State.ARRIVING;
				break;

			case ARRIVING:
				
				System.out.println("\n *********Elevator ARRIVING state...********* \n");

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
	
	/**
	 * Method prints out information about what the elevator is currently doing
	 * @param entry Is the current request that is being processed by the Elevator Subsystem
	 */
	public void triggerElevator(Map.Entry<Integer, Request> entry) {

		request = entry.getValue();

		if (this.currFloor == request.getStartFloor()) {
			if (request.getDirection() == "UP") {
				System.out.println(
						getEleName() + " going up to floor " + request.getDestFloor() + " to drop off passenger.");
			} else {
				System.out.println(
						getEleName() + " going down to floor " + request.getDestFloor() + " to drop off passenger.");
			}
		} else if (this.currFloor != request.getStartFloor()) {
			if (this.currFloor < request.getStartFloor()) {
				System.out.println(
						getEleName() + " going up to floor " + request.getStartFloor() + " to pick up passenger.");
			} else {
				System.out.println(
						getEleName() + " going down to floor " + request.getStartFloor() + " to pick up passenger.");
			}
		}
	}

	/**
	 * Method controls the motor for the elevator, so this method either turns on the motor or turns it off
	 */
	public void triggerMotor() {
		if (!motorOn) {

			motorOn = true;
			System.out.println("Motor is running.");

		} else if (motorOn) {

			motorOn = false;
			System.out.println("Motor is off.");
		}
	}
	
	/**
	 * Method prints arrival information of the elevator when it reached its destination
	 * @param entry Is the current request that is being processed by the Elevator Subsystem
	 */
	public void sendArrivalInfo(Map.Entry<Integer, Request> entry) {
		request = entry.getValue();

		if (this.currFloor == request.getStartFloor()) {
			System.out.println(getEleName() + " has arrived at floor " + request.getStartFloor() + ".");
		} else if (this.currFloor == request.getDestFloor()) {
			System.out.println(getEleName() + " has arrived at floor " + request.getDestFloor() + ".");
		}
	}
	
	/**
	 * @return ArrayList of buttons in the elevator 
	 */
	public ArrayList<Boolean> getListOfButtons() {
		return listOfButtons;
	}
	
	/**
	 * @return ArrayList of lamps in the elevator which let users know of button presses
	 */
	public ArrayList<Boolean> getListOfLamps() {
		return listOfLamps;
	}
	
	/**
	 * Method lets one change the ArrayList of buttons
	 * @param listOfButtons is an ArrayList containing the buttons
	 */
	public void setListOfButtons(ArrayList<Boolean> listOfButtons) {
		this.listOfButtons = listOfButtons;
	}
	
	/**
	 * Method lets one change the ArrayList of lamps
	 * @param listOfLamps is an ArrayList containing the lamps
	 */
	public void setListOfLamps(ArrayList<Boolean> listOfLamps) {
		this.listOfLamps = listOfLamps;
	}
	
	/**
	 * @return name of the elevator
	 */
	public String getEleName() {
		return this.eleName;
	}
	
	/**
	 * Method gets the current floor of the elevator
	 * @return current floor
	 */
	public int getCurrFloor() {
		return currFloor;
	}
	
	/**
	 * Method returns the Key of a map entry which is basically a unique ID for each request
	 * @param entry Current request that is being processed by the Elevator Subsystem
	 * @return Key of the Map entry
	 */
	public int getId(Map.Entry<Integer, Request> entry) {
		return entry.getKey();
	}
	
	/**
	 * Method lets one set the direction of the elevator
	 * @param goUp boolean value of whether the elevator is going up or not
	 */
	public void setDirectionUp(Boolean goUp) {
		this.goUp = goUp;
	}
	
	/**
	 * Method lets one set the direction of the elevator
	 * @param goDown boolean value of whether the elevator is going down or not
	 */
	public void setDirectionDown(Boolean goDown) {
		this.goDown = goDown;
	}
	
	/**
	 * Method lets one know if the elevator is going up
	 * @return goUp boolean value of whether the elevator is going up or not
	 */
	public Boolean getDirectionUp() {
		return this.goUp;
	}
	
	/**
	 * Method lets one know if the elevator is going down
	 * @return goDown boolean value of whether the elevator is going down or not
	 */
	public Boolean getDirectionDown() {
		return this.goDown;
	}
	
	/**
	 * Method lets one set the state of the doors for the elevator
	 * @param closed boolean value if the door is closed or not
	 */
	public void setDoorClosed(Boolean closed) {
		this.doorIsClosed = closed;
	}
	
	/**
	 * Method closes the doors of the elevator
	 */
	public void doorClose() {
		setDoorClosed(true);
		System.out.println("The Elevator is closing doors");
	}
	
	/**
	 * Method opens the doors of the elevator
	 */
	public void doorOpen() {
		setDoorClosed(false);
		System.out.println("The Elevator is opening doors \n");
	}
	
	/**
	 * Method returns the destination floor of the elevator
	 * @return destination floor
	 */
	public int getDestinationFloor() {
		return this.destinationFloor;
	}
	
	/**
	 * Method lets one set the destination floor of the elevator
	 * @param floor destination floor
	 */
	public void setDestinationFloor(int floor) {
		this.destinationFloor = floor;
	}
	
	/**
	 * Internal method for the elevator to use, which sets the destination floor and
	 * turns on the appropriate elevator buttons/lamps
	 * @param floor destination floor
	 */
	public void userDestination(int floor) {
		setDestinationFloor(floor);
		getListOfButtons().set(floor, true);
		getListOfLamps().set(floor, true);
	}
	
	/**
	 * Internal method for the elevator to use, which turns off the appropriate buttons/lamps and
	 * opens/closes the doors once a destination is reached
	 * @param floor destination floor
	 */
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
			Thread.sleep(preDelay);

		} catch (InterruptedException e) {
			System.out.println("There is an error closing the door \n");
			e.printStackTrace();
		}
	}
	
	/**
	 * @return Instance of scheduler
	 */
	public Scheduler getScheduler() {
		return this.scheduler;
	}
	
	/**
	 * @return state of motor, whether its on or off
	 */
	public boolean getMotorOn() {
		return this.motorOn;
	}
	
	/**
	 * @return state of the door, whether its closed or not
	 */
	public boolean getDoorClosed() {
		return this.doorIsClosed;
	}

}
