package elevator_concurrency;

public enum RequestState {
	ELEVATOR_REQUEST, // elevator is notified
	FLOOR_REQUEST, // floor sub system has requests the floor
	REQUEST_WAIT // no request in the queue
}
