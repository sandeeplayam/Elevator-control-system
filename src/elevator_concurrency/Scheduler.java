/**
 * 
 */
package elevator_concurrency;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Tura
 *
 */
public class Scheduler extends Thread {
	private boolean nextRequest = false;
	private List<String> requestList = new ArrayList<String>();

	public Scheduler(String name) {
		super(name);
		requestList.add("e");
	}

	// floor inserts a requests
	synchronized public void scheduleRequest(String request) { // HashMap<String, String>
		while (nextRequest) {
			try {
				wait();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		System.out.println(request);
		nextRequest = true;
		notifyAll();
	}

	// Elevator answers a requests
	synchronized public void excuteRequest(String request) { // HashMap<String, String> request
		while (!nextRequest) {
			try {
				wait();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		System.out.println(request);
		nextRequest = false;
		notifyAll();
	}

	public boolean isRequest() {
		return requestList.isEmpty();
	}

//	synchronized public void run() {
//
//		for (;;) {
//			while (isRequest()) {
//				System.out.println("waiting for request");
//				try {
//					wait();
//				} catch (Exception e) {
//					System.err.println(e);
//				}
//			}
//		}
//	}

	public static void main(String[] args) {
		List<String> floors = new ArrayList<String>();
		floors.add("floor1");
		floors.add("floor2");
		floors.add("floor3");
		floors.add("floor4");
		floors.add("floor5");
		Scheduler scheduler = new Scheduler("scheduler");
		FloorSubsystem floor = new FloorSubsystem("Floor ", floors, scheduler);
		ElevatorSubsystem elevator = new ElevatorSubsystem("elevator #1", scheduler);
		scheduler.start();
		floor.start();
		elevator.start();
	}
}
