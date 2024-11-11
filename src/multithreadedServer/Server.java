package multithreadedServer;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	// server is going to have ServerSocket and default port number 
	private ServerSocket server;
	private static final int DEFAULT_PORT = 88; // since we are not going to change it
	
	// Constructor
	public Server(){
		try {
			this.server = new ServerSocket(this.getDEFAULT_PORT()); 
			System.out.println("Server opened at " + Inet4Address.getLocalHost());
		}
		catch (IOException e) {
			System.out.println("Problem occured in creating server socket. Please see the below error message:");
			e.printStackTrace();
		}
		// to accept every client request
		while (true) {
			this.clientAccept();
		}
	}
	
	public void clientAccept() {
		try {
			Socket client = this.server.accept(); // accept the client's connection
			// since server is going to wait till client connects we need to first accept the connection request and in server
			System.out.println("Connection is created with the client at the adress of: " + client.getInetAddress()); // printout the address of the client to the console
			// Since we are going to accept more than one server (multithreading) we need to generate Threaded server as follow:
			ServerThread serverThread = new ServerThread(client);
			serverThread.start(); // to start the client's interaction with the server!
		}
		catch (IOException e) {
			System.out.println("There had been problem connecting client to the server. Please see the below error message: ");
			e.printStackTrace();
			
		}
		
	}
	
	//TODO: send data to client

	public ServerSocket getServer() {
		return server;
	}

	public void setServer(ServerSocket server) {
		this.server = server;
	}

	public int getDEFAULT_PORT() {
		return DEFAULT_PORT;
	}
	

}
