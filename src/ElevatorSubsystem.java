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

	synchronized public void run() {
		for (;;) {
			Map.Entry<Integer, Request> entry = this.scheduler.executeRequest();
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
				System.err.println(e);

			}
			System.out.println(Thread.currentThread().getName() + ": " + entry.getValue());
			System.out.println("-------------------------------------------------------------");
			this.scheduler.runCompleted();
		}
	}

}
