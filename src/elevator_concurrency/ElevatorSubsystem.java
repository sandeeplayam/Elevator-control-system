/**
 * 
 */
package elevator_concurrency;

/**
 * @author danie
 *
 */
public class ElevatorSubsystem extends Thread {
	private Scheduler scheduler;

	public ElevatorSubsystem(String name, Scheduler scheduler) {
		super(name);
		this.scheduler = scheduler;
	}

	public void run() {
		for (;;)
			this.scheduler.excuteRequest("Elevator answered");
	}

}
