/**
 * @author Group #2
 *
 */

public enum SchedulerStates {
	ELEVATOR_REQUEST, // elevator is notified
	FLOOR_REQUEST, // floor sub system has requests the floor
	SCHEDULER, // scheduler is figuring out the next move
	SLEEP, // no request in the queue
	ElevatorRunning;

	public boolean compare(SchedulerStates r) {
		if (this == r)
			return true;
		return false;
	}
}