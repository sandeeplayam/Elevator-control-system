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
			this.floorReceive();

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

	public void run() {
		this.floorSend();
	}

	public static void main(String[] args) throws UnknownHostException {

		FloorSubsystemUdp fl = new FloorSubsystemUdp("172.17.155.10");
		fl.start();
	}

}
