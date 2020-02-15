import java.util.Map;

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
		for (;;) {
			Map.Entry<Integer, Request> entry = this.scheduler.excuteRequest();
			System.out.println(Thread.currentThread().getName() + ": " + entry.getValue());
		}
	}

}
