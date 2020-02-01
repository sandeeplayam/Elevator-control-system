import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
/**
 * Elevator sub system class for Sysc 3303 Iteration 1 group 2
 * @author 	Sudarsana Sandeep 100963087
 *
 */
public class ElevatorSubSystem {
	
	private static int preDelay = 1;
	private static int postDelay = 3;
	private ArrayList<Boolean> listOfButtons;
	private ArrayList<Boolean> listOfLamps;
	private int currFloor;
	private int destinationFloor;
	private Boolean goUp;
	private Boolean goDown;
	private Boolean ON;
	private int eleNumber;
	private Boolean doorIsClosed;
	
	public ElevatorSubSystem (int elevatorNumber, int buttons) {
		
		this.eleNumber = elevatorNumber;
		doorIsClosed = true; //initially setting the door of the elevator to closed
		currFloor = 1; //setting the floor of the elevator to 1 when initialized 
		ON = false; //initially setting the elevator to off
		
		listOfButtons = new ArrayList<Boolean>(Arrays.asList(new Boolean[buttons])); //instantiating the list of buttons
		Collections.fill(listOfButtons, Boolean.FALSE); //filling the list with false boolean values
		
		listOfLamps = new ArrayList<Boolean>(Arrays.asList(new Boolean[buttons])); //instantiating the list of lamps
		Collections.fill(listOfLamps, Boolean.FALSE); //filling the list with false boolean values
	}
	
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
	
	public int getEleNumber () {
		return eleNumber;
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
		return this.ON;
	}
	
	public void doorClose () {
		setDoorClosed(true);
		System.out.println("The Elevator is closing \n");
	}
	
	public void doorOpen () {
		setDoorClosed(false);
		System.out.println("The Elevator is opening \n");
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
			TimeUnit.SECONDS.sleep(preDelay);
			doorOpen();
		} catch (InterruptedException e) {
			System.out.println("There is an error closing the door \n");
			e.printStackTrace();
		}

		try {
			TimeUnit.SECONDS.sleep(postDelay);
			doorClose();
		} catch (InterruptedException e) {
			System.out.println("There is an error closing the door \n");
			e.printStackTrace();
		}
	}
	
}
