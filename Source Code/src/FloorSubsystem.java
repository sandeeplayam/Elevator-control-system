import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.util.*;

public class FloorSubsystem extends Thread {
	private DatagramSocket sendReceiveSocket;
	private DatagramPacket sendPacket, receivePacket;
	private final InetAddress SchedulerInetAdress;
	
	private ArrayList<String> AL = new ArrayList<String>();

	public FloorSubsystem(String ip) throws UnknownHostException {
		this.SchedulerInetAdress = InetAddress.getByName(ip);
	}

	/***
	 * Initialize socket and bind it to local host. Since this class act as
	 * client,bind to machine is enough
	 */
	public void initializeSocket() {
		try {
			this.sendReceiveSocket = new DatagramSocket();
		} catch (SocketException se) { // Can't create the socket.
			se.printStackTrace();
			System.exit(1);
		}
	}

	/***
	 * 
	 * Prepare a DatagramPacket and send it via sendReceiveSocket to port 5000 on
	 * the destination host.
	 * @throws FileNotFoundException 
	 */

	public void floorSend() throws FileNotFoundException {
		
		
			
		if(AL.size()>=1) {
			
			try {
			
			this.initializeSocket();
			
			String s = AL.remove(0);
			
			
			System.out.println("Client: sending a packet containing: " + s);

			// change the string to byte
			byte msg[] = s.getBytes();

			Thread.sleep(2000);

			// prepare the message to be sent over port 3000
			this.sendPacket = new DatagramPacket(msg, msg.length, SchedulerInetAdress, 3000);

			// Send the datagram packet to the server via the send socket.
			sendReceiveSocket.send(this.sendPacket);
			System.out.println("Packet sent!!!");
			sendReceiveSocket.close();

			// get the response of the server(scheduler)
			this.floorReceive();
		}
		 catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
		
		else {
			
			System.out.println("No request.");
		}
		
	
	}

	/***
	 * wait for the server(scheduler) to respond with a message
	 * @throws InterruptedException 
	 */
	public void floorReceive() throws InterruptedException {

		try {

			// Initialize socket and listen to port 3000
			this.sendReceiveSocket = new DatagramSocket(3003);

			// Construct a DatagramPacket for receiving packets up
			// to 100 bytes long
			byte data[] = new byte[100];
			receivePacket = new DatagramPacket(data, data.length);

			sendReceiveSocket.receive(receivePacket);
			// Process the received datagram.
			System.out.print("Packet received contaning:\t");
			int len = receivePacket.getLength();

			// Form a String from the byte array.
			String received = new String(data, 0, len);
			System.out.println(received);

			// We're finished, so close the sockets.
			sendReceiveSocket.close();

			// send the received packet to floor
			
			Thread.sleep(3000);
			this.floorSend();
		} catch (SocketException se) { // Can't create the socket.
			se.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			System.out.print("IO Exception: likely:");
			System.out.println("Receive Socket Timed Out.\n" + e);
			e.printStackTrace();
			System.exit(1);
		}

	}
	
	public void requested() throws FileNotFoundException {
		
		File file = new File("./elevator.txt");
		Scanner scan = new Scanner(file);

		while (scan.hasNextLine()) {

			String line = scan.nextLine();
			String[] info = line.split(" ");
			EventErrorHandler error = new EventErrorHandler(info);
			

			if (error.strChecker() == true) {
				if (info.length == 4) {

					LocalTime time = LocalTime.parse(info[0]);


					int fNumber = Integer.parseInt(info[1]);

					String dir = info[2];
					dir = dir.toUpperCase(); // making the direction string upper case so its standard throughout the
												// system

					int dNumber = Integer.parseInt(info[3]);
					
					Request r = new Request(time, fNumber, dir, dNumber);
					
					String convertedString = String.valueOf(r);//convert Request to type string
					
					AL.add(convertedString);//add String to array list AL
					
					

				} else {
					System.out.print("Did not receive the correct inputs, cannot process request. \n");
					continue;
				}
			} else {
				continue;
			}
		}
		scan.close();
		
	}

	public void run() {
		
		try {
			this.requested();
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}

		try {
			Thread.sleep(6500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		try {
			this.floorSend();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws UnknownHostException {

		FloorSubsystem fl = new FloorSubsystem("172.17.168.190");
		fl.start();
	}

}
