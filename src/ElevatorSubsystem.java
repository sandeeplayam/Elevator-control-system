import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
/**
 * Elevator sub system class for Sysc 3303 Iteration 1 group 2
 * @author 	Sudarsana Sandeep 100963087
 *
 */
public class ElevatorSubSystem extends Thread {
	
	private static int preDelay = 2;
	private static int postDelay = 3;
	static private int timeMotor = 3;
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
	//private HashMap<Integer, Request> requestList;
	State state = State.IDLE;
	private String eleName;
	//public ArrayList<Request> requestList;
	
	public ElevatorSubSystem (String name, Scheduler scheduler) {
		
		this.eleName = name;
		doorIsClosed = true; //initially setting the door of the elevator to closed
		
		this.currFloor = 1;
		this.scheduler = scheduler;
		
		listOfButtons = new ArrayList<Boolean>(Arrays.asList(new Boolean[22])); //instantiating the list of buttons
		Collections.fill(listOfButtons, Boolean.FALSE); //filling the list with false boolean values
		
		listOfLamps = new ArrayList<Boolean>(Arrays.asList(new Boolean[22])); //instantiating the list of lamps
		Collections.fill(listOfLamps, Boolean.FALSE); //filling the list with false boolean values
		
		
		System.out.println("Elevator " + getEleName() + " ready, waiting for requests from the Scheduler in an idle state.");
	}
	
	public void run() {
		for (;;) {
			
			Map.Entry<Integer, Request> entry = this.scheduler.excuteRequest();
			System.out.println(Thread.currentThread().getName() + ": " + entry.getValue());
			elevatorState(entry);
			
		}
	}
	
	public enum State {
		IDLE, RUN, ARRIVING;
	}
	
	public void elevatorState (Map.Entry<Integer, Request> entry) {
		
		request = entry.getValue();
		boolean finished = false;
		
		while (!finished) {
			
			switch (state) {
			
			case IDLE:
				
				System.out.println("Elevator has received a request from the scheduler, processing request.");
				if (this.currFloor == request.getStartFloor()) {
					
					try {
						doorOpen();
						TimeUnit.SECONDS.wait(preDelay);
					} catch (InterruptedException e) {
						System.out.println("There is an error in opening the door \n");
						e.printStackTrace();
					}
					
					try {
						doorClose();
						TimeUnit.SECONDS.wait(preDelay);
					} catch (InterruptedException e) {
						System.out.println("There is an error in opening the door \n");
						e.printStackTrace();
					}
					state = State.RUN;
				}
				
				if (this.currFloor != request.getStartFloor()) {
					state = State.RUN;
				}
				
				break;
				
			case RUN:
				int diff = Math.abs(this.currFloor - request.getStartFloor());
				
				if (this.currFloor == request.getStartFloor()) {
					userDestination(request.getDestFloor());
					diff =  Math.abs(this.currFloor - request.getDestFloor());

				} 
				
				try {
					triggerMotor();
					triggerElevator(entry);
					while (diff != 0) {
						TimeUnit.SECONDS.wait(timeMotor);
						this.currFloor++;
						if (this.currFloor != request.getStartFloor() || this.currFloor != request.getDestFloor()) {
							System.out.println("Elevator " + this.getEleName() + " currently at floor " + this.currFloor);
						}
						diff--;
					}
				} catch (InterruptedException e) {
					System.out.println("Error running the motor \n");
					e.printStackTrace();
				}
				
				state = State.ARRIVING;
				break;
				
			case ARRIVING:
				
				sendArrivalInfo();
				
				try {
					doorOpen();
					TimeUnit.SECONDS.wait(preDelay);
				} catch (InterruptedException e) {
					System.out.println("There is an error in opening the door \n");
					e.printStackTrace();
				}
				
				try {
					doorClose();
					TimeUnit.SECONDS.wait(preDelay);
				} catch (InterruptedException e) {
					System.out.println("There is an error in opening the door \n");
					e.printStackTrace();
				}
				
				if (this.currFloor == request.getDestFloor()) {
					finished = true;
					state = State.IDLE;
				} else {
					state = State.RUN;
				}
				
				break;
			}

		}
			
		
	}
	
	public void triggerElevator (Map.Entry<Integer, Request> entry) {
		
		request = entry.getValue();
		
		if ()	
			if (request.getDirection() == "UP") {
				System.out.println("Elevator" + getEleName() + "going up to " + request.getDestFloor() + ".");
			} else {
				System.out.println("Elevator" + getEleName() + "going down to" + request.getDestFloor() + ".");
			}	
		
	}
	
	public void triggerMotor() {
		if (!motorOn) {
			
			motorOn = true;
			System.out.println("Motor is running");
			
		} else if (motorOn) {
			
			motorOn = false;
			
		}
	}
	
	public void sendArrivalInfo() {
		
	}
	
	/*public void addRequest (Integer key, Request value) {
		requestList.put(key, value);
	}*/
	
	public ArrayList<Boolean> getListOfButtons () {
		return listOfButtons;
	}
	
	public ArrayList<Boolean> getListOfLamps () {
		return listOfLamps;
	}
	
	public void setListOfButtons (ArrayList<Boolean> listOfButtons) {
		this.listOfButtons = listOfButtons;
	}
	
	public void setListOfLamps (ArrayList<Boolean> listOfLamps) {
		this.listOfLamps = listOfLamps;
	}
	
	public String getEleName () {
		return eleName;
	}
	
	public int getCurrFloor () {
		return currFloor;
	}
	
	public void setDirectionUp (Boolean goUp) {
		this.goUp = goUp;
	}
	
	public void setDirectionDown (Boolean goDown) {
		this.goDown = goDown;
	}
	
	public Boolean getDirectionUp () {
		return this.goUp;
	}
	
	public Boolean getDirectionDown () {
		return this.goDown;
	}
	
	public void setDoorClosed (Boolean closed) {
		this.doorIsClosed = closed;
	}
	
	public Boolean isOn () {
		return this.functioning;
	}
	
	public void doorClose () {
		setDoorClosed(true);
		System.out.println("The Elevator is closing doors \n");
	}
	
	public void doorOpen () {
		setDoorClosed(false);
		System.out.println("The Elevator is opening doors \n");
	}
	
	public int getDestinationFloor () {
		return this.destinationFloor;
	}
	
	public void setDestinationFloor (int floor) {
		this.destinationFloor = floor;
	}
	
	public void userDestination (int floor) {
		setDestinationFloor(floor);
		getListOfButtons().set(floor, true);
		getListOfLamps().set(floor, true);
	}
	
	public void reachedDestination (int floor) {
		getListOfButtons().set(floor, false);
		getListOfLamps().set(floor, false);
		try {
			TimeUnit.SECONDS.wait(preDelay);
			doorOpen();
		} catch (InterruptedException e) {
			System.out.println("There is an error closing the door \n");
			e.printStackTrace();
		}

		try {
			TimeUnit.SECONDS.wait(postDelay);
			doorClose();
		} catch (InterruptedException e) {
			System.out.println("There is an error closing the door \n");
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Scheduler scheduler = new Scheduler("scheduler");
		ElevatorSubSystem ele = new ElevatorSubSystem("1", scheduler);
	}
	
}
