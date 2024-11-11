package multithreadedServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URISyntaxException;

public class ServerThread extends Thread{ // also implements Runnable would work too
	// We are going to be needing the followings:
	protected BufferedReader input; // only those who extends from this class can use this
	protected PrintWriter output;
	Socket client; // client socket
	
	// TODO: change after you understand!
	private String clientRequest = new String(); //what client requested
	
	private Alpha416Api alphaServer;
	
	// constructor
	public ServerThread(Socket client) throws IOException {
		this.client = client; // initialize client!
		alphaServer = new Alpha416Api();
	}
	
	@Override
	// We are going to override the method run from Thread class to be able to make multithreading
	public void run() {
		// what am I going to run in parallel:
		try {
			input = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
			output = new PrintWriter(this.client.getOutputStream());
			
			this.clientRequest = input.readLine();
			while (this.clientRequest != null && !clientRequest.equalsIgnoreCase("quit")) { // meaning while client do not say quit
				
				// I am not going to need this though when I implement API
				//TODO: Change while implementing API!
				
				System.out.println("Client " + this.client.getRemoteSocketAddress() + " sent : " + this.clientRequest);
                String[] reqArr = this.clientRequest.split(" ");
                if (reqArr.length>1) { // if request arr is not empty
	                String message = reqArr[1];
	                if (message.compareToIgnoreCase("exc") == 0) { // if it is exchange
	                	this.handleExchangeRequest(reqArr);
	            				
	            	}
	                else if (message.compareToIgnoreCase("gas") == 0) {
	                	this.handleGasRequest(reqArr);
	                			
	                }
	                else {
	                	output.print("Alpha416 ALPHA_500 Server Fault\n");
	                }
	                this.clientRequest = input.readLine();
                }
	               
                // Getting request from the client
				//TODO: send the request get from the client to API 
				//TODO: receive response from the API
				//TODO: send response to the client
			}			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			this.close();
		}
	
		
	}
	public void close() {
		try {
			if (this.input != null) { // if input hasn't been closed
				this.input.close();
			}
			if (this.output != null) { // if output stream hasn't been closed
				this.output.close();
			}
			
			if (this.client != null) { // if client hasn't been closed (which is the most important)
				this.client.close();
			}
			System.out.println("Alpha416 QUIT");	
		}
		catch (IOException e) {
			System.out.println("Some problems occured while trying to close connection. Please see the below error message: ");
			e.printStackTrace();
		}
	}
	
	private void handleExchangeRequest(String[] reqArr) throws IOException, URISyntaxException {
		String code1 = reqArr[3];
		String code2 = reqArr[5];
		String data = alphaServer.getConnectExchange(code1, code2);

		if (reqArr.length == 8) {
    		if (data == null || alphaServer.parseRefreshed(data) == null) {
    			output.print("Alpha416 ALPHA_400 Invalid Request\nError: Invalid parameters for EXC command.\n");
    		}
    		else if (alphaServer.parseRefreshed(data).startsWith("Alpha416")) {
    			output.println(alphaServer.parseRefreshed(data));
    		}
    		else{
    				
    			output.print("Alpha416 ALPHA_200 Success\n" + alphaServer.parseExchange(data) +
    					", " + alphaServer.parseRefreshed(data) + "\n");
    			
    		}
    		output.println("END"); // to know that the output is ended!!!
    		output.flush();
    	}
    	else {
    		data = alphaServer.getConnectExchange(code1, code2);
    		if (alphaServer.parseExchange(data) == null) {
    			output.print("Alpha416 ALPHA_400 Invalid Request\nError: Invalid parameters for EXC command.\n");
    		}
    		else if (alphaServer.parseRefreshed(data).startsWith("Alpha416")) {
    			output.println(alphaServer.parseRefreshed(data));
    		}
    		else {
    			output.println("Alpha416 ALPHA_200 Success\n" + alphaServer.parseExchange(data) + "\n");
    		}
    		output.println("END"); // to know that the output is ended!!!
    		output.flush();
    	}
	    
	}
	
	private void handleGasRequest(String[] reqArr) throws URISyntaxException, IOException {
		
		String date1 = reqArr[3];
		String data = alphaServer.getConnectGas();
		if (reqArr.length == 4) {
			if (alphaServer.parsePrice(data).startsWith("Alpha416")) {
				output.println(alphaServer.parsePrice(data));
			}
			else if (alphaServer.findDate(data, date1) == null) {
				output.print("Alpha416 ALPHA_404 Not Found\nError: Invalid date for GAS command.\n");
			}
			else {
				output.print("Alpha416 ALPHA_200 Success\n" + alphaServer.findDate(data, date1) + "\n");
			}
			output.println("END"); // to know that the output is ended!!!
			output.flush();
			
		}
		else {
			String func = reqArr[4];
			String date2 = reqArr[5];
			if (func.compareTo("-change") == 0) {              		
				if (alphaServer.findDate(data, date2) == null || 
		 				alphaServer.findDate(data, date1) == null) {
					output.print("Alpha416 ALPHA_404 Not Found\nError: Invalid date for GAS command.\n");
				}
				else if (alphaServer.parsePrice(data).startsWith("Alpha416")) {
					output.println(alphaServer.parsePrice(data));
				}
				else {
					output.println("Alpha416 ALPHA_200 Success\n" + alphaServer.change(data, date1, date2));
					
				}
				output.println("END"); // to know that the output is ended!!!
				output.flush();
				
			}
			else {
				if (alphaServer.findDate(data, date2) == null || 
						alphaServer.findDate(data, date1) == null) {
					output.print("Alpha416 ALPHA_404 Not Found\nError: Invalid date for GAS command.\n");
					
				}
				else if (alphaServer.parsePrice(data).startsWith("Alpha416")) {
					output.println(alphaServer.parsePrice(data));
				}
				else {
					output.println("Alpha416 ALPHA_200 Success\n" + alphaServer.average(data, date1, date2));
				}
				output.println("END"); // to know that the output is ended!!!
				output.flush();
			}
		}
		
	}
	
	

}
