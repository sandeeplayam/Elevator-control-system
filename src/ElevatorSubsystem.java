/**
 * 
 */

/**
 * @author Group #2
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
			this.scheduler.excuteRequest("Elevator responded to the Floor request\n");
	}

}
