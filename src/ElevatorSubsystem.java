import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
	private ArrayList<Request> requestList;
	State state = State.IDLE;
	private String eleName;
	private DatagramSocket sendSocket, receiveSocket;
	private DatagramPacket sendPacket, receivePacket;
	private int elePort;
	private InetAddress schedulerAddress;
	
	/**
	 * Constructor for the Elevator Subsystem 
	 * @param name The name for the Elevator Subsystem being constructed 
	 * @param scheduler Passing an instance of the scheduler into the elevator subsystem
	 * @throws UnknownHostException 
	 */
	public ElevatorSubsystem(String name, String ip) throws UnknownHostException {

		super(name);
		this.eleName=name;
		doorIsClosed = true; // initially setting the door of the elevator to closed
		
		this.schedulerAddress = InetAddress.getByName(ip);

		this.currFloor = (int) (10 * Math.random());
		
		String[] splitP = name.split(" ");
		
		elePort = 2000 + Integer.parseInt(splitP[1]);
		

		listOfButtons = new ArrayList<Boolean>(Arrays.asList(new Boolean[22])); // instantiating the list of buttons
		Collections.fill(listOfButtons, Boolean.FALSE); // filling the list with false boolean values

		listOfLamps = new ArrayList<Boolean>(Arrays.asList(new Boolean[22])); // instantiating the list of lamps
		Collections.fill(listOfLamps, Boolean.FALSE); // filling the list with false boolean values

		requestList = new  ArrayList<Request>();
		
	}
	
	/**
	 * Since ElevatorSubsystem extends Thread, it requires an override of the run method.
	 * Overridden ElevatorSubsystem run method waits for a request from the scheduler and
	 * starts the state machine. 
	 */
	public void run() {
		
			this.receiveSchedulerRequest();
			//Map.Entry<Integer, Request> entry = this.scheduler.executeRequest();
			//this.requestList.put(entry.getKey(), entry.getValue());

			System.out.println("--------------------------------------End of Request-----------------------------------\n\n");
			

		
	}
	
	/***
	 * Initialize socket and bind it to port 3001
	 */
	public void receiveSchedulerRequest() {

		try {

			// Initialize socket and listen to port 300x (x is based on the elevator #, so each elevator has its own port)
			this.receiveSocket = new DatagramSocket(elePort);

			// Construct a DatagramPacket for receiving packets up
			// to 100 bytes long
			byte data[] = new byte[100];
			receivePacket = new DatagramPacket(data, data.length);
			System.out.println("Elevator: Waiting for request.");
			receiveSocket.receive(receivePacket);
			Thread.sleep(2000);
			// Process the received datagram.
			int len = receivePacket.getLength();

			// Form a String from the byte array.
			String received = new String(data, 0, len);
			
			requestList.add(this.stringToRequest(received));
			
			System.out.print("Packet received contaning:\t");
			System.out.println(received);

			// We're finished, so close the sockets.
			receiveSocket.close();
			this.elevatorState();
			//this.sendRequestToScheduler(data);
		} catch (SocketException se) { // Can't create the socket.
			se.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			System.out.print("IO Exception: likely:");
			System.out.println("Receive Socket Timed Out.		" + e);
			e.printStackTrace();
			System.exit(1);
		} catch (InterruptedException e) {
		
			e.printStackTrace();
		}
	}
	
	public void sendRequestToScheduler(byte[] data) {

		try {
			// initialize the socket and packet from the received data
			this.sendSocket = new DatagramSocket();
			
			

			
			sendPacket = new DatagramPacket(data, data.length, this.schedulerAddress, 2000);
			System.out.print("Containing: ");
			System.out.println(new String(sendPacket.getData(), 0, sendPacket.getLength()));
			Thread.sleep(2000);
			sendSocket.send(sendPacket);

			// Slow things down (wait 2 seconds)

			System.out.println("Server: packet sent");

			// We're finished, so close the sockets.
			sendSocket.close();
			//this.receiveSchedulerRequest();

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (InterruptedException e) {
			
			e.printStackTrace();

		}
	}
	
	public Request stringToRequest(String s) {
		String[] sA = s.split(" ");
		return new Request(LocalTime.parse(sA[0]), Integer.parseInt(sA[1]), sA[2], Integer.parseInt(sA[3]));
	}

	
	public enum State {
		IDLE, RUN, ARRIVING;
	}
	
	/**
	 * This method is the FSM of the ElevatorSubsystem 
	 * @param entry Is the current request that is about to be processed by the FSM
	 */
	public void elevatorState() {

		request = requestList.get(0);
		
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
					triggerElevator();

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

				sendArrivalInfo();
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
					requestList.remove(0);
					finished = true;
					state = State.IDLE;
				} else {
					state = State.RUN;
				}

				break;
			}

		}
		String s = request.toString();
		s = s + " " + this.eleName + " " + this.currFloor;
		byte msg[] = s.getBytes();
		this.sendRequestToScheduler(msg);
		this.receiveSchedulerRequest();

	}
	
	/**
	 * Method prints out information about what the elevator is currently doing
	 * @param entry Is the current request that is being processed by the Elevator Subsystem
	 */
	public void triggerElevator() {

		request = requestList.get(0);

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
	public void sendArrivalInfo() {
		request = requestList.get(0);

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
	
	public static void main(String[] args) throws UnknownHostException {

		ElevatorSubsystem elev = new ElevatorSubsystem("Elevator 1", "172.17.168.190");
		ElevatorSubsystem elev2 = new ElevatorSubsystem("Elevator 2", "172.17.168.190");
		ElevatorSubsystem elev3 = new ElevatorSubsystem("Elevator 3", "172.17.168.190");
		
		
		
		String s = "x x x x " + elev.eleName + " " + elev.getCurrFloor();
		byte msg[] = new byte[100];
		msg = s.getBytes();
		elev.sendRequestToScheduler(msg);
		elev.start();
		
		
		String s2 = "x x x x " + elev2.eleName + " " + elev2.getCurrFloor();
		//byte msg[] = new byte[100]
		msg = s2.getBytes();
		elev2.sendRequestToScheduler(msg);
		elev2.start();
		
		
		String s3 = "x x x x " + elev3.eleName + " " + elev3.getCurrFloor();
		//byte msg[] = new byte[100];
		msg = s3.getBytes();
		elev3.sendRequestToScheduler(msg);
		elev3.start();
	}

}
