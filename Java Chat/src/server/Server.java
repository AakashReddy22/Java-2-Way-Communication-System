package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class Server {
	private static DatagramSocket socket;
	
	// var telling us if the server is running or not
	private static boolean running;
	private static int ClientID;
	private static ArrayList<ClientInfo> clients= new ArrayList<ClientInfo>();
	
	// Will start our server and create the desired resources that we need for the program
	public static void start(int port) {
		try {
	 		//initialize our socket
			socket = new DatagramSocket(port);
			running = true;
			// Calling our listen method,which will start the thread at line 49
			listen();
			System.out.println("Server has started on port, "+port);
			
		} catch (Exception e) {
				e.printStackTrace();
		}		
	}
	
	// This method will send the desired message to every connected client on the web server
	private static void broadcast(String message) {
		for (ClientInfo info : clients) {
			send(message, info.getAddress(), info.getPort());
		}
	}
	
	// Private because we aren't going to use this class in out ChatServer class
	private static void send(String message, InetAddress address, int port) {
		try {
			// Creating the end identifier into "message"
			message += "\\e";
			// converting message string into a byte array
			byte[] data = message.getBytes();
			// This is the organization of the message that were are sending
			DatagramPacket packet = new DatagramPacket(data, data.length,address, port);
			socket.send(packet);
			System.out.println("Sent message to, "+address.getHostAddress()+":"+port);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Waiting for messages in the server; will always be running when the server is on
	private static void listen() {
		Thread listenThread = new Thread("ChatProgram Listener"){
			// When the thread is started, this run method gets initiated
			public void run() {
				try {
					// While the server is running we are going to create a byte array and a DatagramPacket that writes to our data variable
					while (running) {
						
						// Byte array to get our data
						byte[] data =  new byte[1024];
						// To get the data we do this
						DatagramPacket packet = new DatagramPacket(data, data.length);
						// Getting the data into the packet to send to the array
						// Were using a packet instead of direct transfer between the socket and the array, because we get access to more information,which allows the server to keep running for as long as possible
						//This is placed after the thread and in the try catch as the program freezes after the listen method in line 17
						socket.receive(packet);
						// careful
						String message = new String(data);
						// Identifier at the end of the message, which is \e to tell the server where the end of our message is so if we want to alter that message,we dont have to search the whole array's 1024 characters 
						message = message.substring(0, message.indexOf("\\e"));
						
						//MANAGE MESSAGE
						if (!isCommand(message, packet)) {
						broadcast(message);
						}
					}
						
			} catch (Exception e) {
				e.printStackTrace();
			}
			}
			// starting the thread
		}; listenThread.start();
		
	}
	/**
	 * Server Command List
	 * \con: [name] -> connects client to server
	 * \dis: [id] -> disconnect client from server
	 * 
	 */
	
	// Checking for connection or disconnection
	private static boolean isCommand (String message, DatagramPacket packet) {
		
		// Array list is adaptive to the program running. The array list will resize the array automatically when the program is running
		if (message.startsWith("\\con:")) {
			// RUN connection code
			
			String name = message.substring(message.indexOf(":")+1);
			
			clients.add(new ClientInfo(name, ClientID++, packet.getAddress(), packet.getPort()));
			broadcast("User,"+name+", Connected to the Server, Welcome!");
			
			return true;
		}
		
		
		return false;
	}
	
	// Stops the server without stopping the program
	public static void stop () {
		running = false;
	}
}
