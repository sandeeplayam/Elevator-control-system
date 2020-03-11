
/**
 * @author Group #2

 *
 */
import java.time.LocalTime;

public class Request {

	private LocalTime time;
	private int startFloor;
	private String direction;
	private int destFloor;

	public Request(LocalTime time, int startFloor, String direction, int destFloor) {
		this.time = time;
		this.startFloor = startFloor;
		this.direction = direction;
		this.destFloor = destFloor;
	}

	public LocalTime getTime() {
		return this.time;
	}

	public int getStartFloor() {
		return this.startFloor;
	}

	public String getDirection() {
		return this.direction;
	}

	public int getDestFloor() {
		return this.destFloor;
	}

	@Override
	public String toString() {
		return this.time + " " + this.startFloor + " " + this.direction + " " + this.destFloor;
	}
	
}