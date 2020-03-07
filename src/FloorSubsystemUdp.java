import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class FloorSubsystemUdp extends Thread {
	private DatagramSocket sendReceiveSocket;
	private DatagramPacket sendPacket, receivePacket;
	private final InetAddress SchedulerInetAdress;

	public FloorSubsystemUdp(String ip) throws UnknownHostException {
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
	 */

	public void floorSend() {
		// start the socket again
		try {
			this.initializeSocket();
			String s = "Floor request";
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
			// this.floorReceive();

		} catch (UnknownHostException e) {
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

	/***
	 * wait for the server(scheduler) to respond with a message
	 */
	public void floorReceive() {
		// start the socket again
		this.initializeSocket();
		byte data[] = new byte[100];
		this.receivePacket = new DatagramPacket(data, data.length);

		try {
			// Block until a datagram is received via sendReceiveSocket.
			sendReceiveSocket.receive(this.receivePacket);

			// Process the received datagram.
			System.out.println("Client: Packet received:");
			int len = this.receivePacket.getLength();
			// Form a String from the byte array.
			String received = new String(data, 0, len);

			System.out.print("Containing: ");
			System.out.println(received);

			// close the socket .
			sendReceiveSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

	public void run() {
		this.floorSend();
	}

	public static void main(String[] args) throws UnknownHostException {

		FloorSubsystemUdp fl = new FloorSubsystemUdp("172.17.155.10");
		fl.start();
	}

}
