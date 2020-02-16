import java.util.*;
import java.io.*;

public class Iteration1Test {

	private Scanner a;
	
	/*Opening the text file
	 * 
	 */
	public void opened() {
		try {
			a = new Scanner (new File ("elevator.txt"));		
		}
		catch(Exception e) {
			System.out.println("Unfortunately file could not be found");
			}
	}
	
	
	/* Reading text file
	 * 
	 */
	public void reading() {
		
		while(a.hasNext()) {
			System.out.println(a.nextLine());	
		}
	}
	/**
	 * This method closes the text file
	 */
	public void closing() {
		a.close();
	}
}





