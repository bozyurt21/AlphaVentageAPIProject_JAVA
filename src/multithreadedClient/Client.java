package multithreadedClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
		public final String DEFAULT_ADDRESS = "localhost"; 
		public final int DEFAULT_PORT = 88;
		
		private Socket client;
		protected BufferedReader input;
		protected PrintWriter output;
		
		public Client() throws IOException {
			try {
				this.client = new Socket(this.DEFAULT_ADDRESS, this.DEFAULT_PORT);
			}
			catch (IOException e) {
				System.out.println("While creating client, there had been some errors please see the below  error message: ");
				e.printStackTrace();
			}
		}
		
		// connecting socket to the server
		public void connect() throws IOException {
			try {
				this.input = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
				this.output = new PrintWriter(this.client.getOutputStream());
				System.out.println("Sucessfully connected to the server at: " + this.client.getRemoteSocketAddress());
			}
			catch (IOException e) {
				System.out.println("Client couldn't connected to server. Please see the below error message: ");
				e.printStackTrace();
			}
		}
		
		public String sendExchangeRequest(String message, String countryCode1, String countryCode2, boolean refresh) throws IOException{

		        String out;
		        if (refresh == true) {
		        	out = "Alpha416 " + message.toUpperCase() + " -from " + countryCode1.toUpperCase() + " -to " + countryCode2.toUpperCase() + " -to_name -refresh";
		        }
		        else {
		        	out = "Alpha416 " + message.toUpperCase() + " -from " + countryCode1.toUpperCase() + " -to " + countryCode2.toUpperCase();
		        }
		        this.output.println(out);
		        this.output.flush();
		        
		        return out;
		}
		
		public String sendGasRequest(String message, String date1, String date2, boolean average) throws IOException {
			String out;
			
			if (date2 != null) {
				if(average) {
					out = "Alpha416 " + message.toUpperCase() + " -date " + date1 + " -average " + date2;
				}
				else {
					out = "Alpha416 " + message.toUpperCase() + " -date " + date1 + " -change " + date2;
				}
			}
			else {
				out = "Alpha416 " + message.toUpperCase() + " -date " + date1;
			}
	        this.output.println(out);
	        this.output.flush();
	        
	        return out;
		}
		
		// TODO: Receive response from the server.
		public void recieveResonse() throws IOException {
			String response = new String();
		    String line = this.input.readLine();
		    while (line != null && !line.equals("END")) { // Stop when "END" is received
		        response += line;
		        response += "\n";
		        line = this.input.readLine();
		    }
		    System.out.println(response);
		}
		
		
		// disconnecting the client from the server.
		public void disconnect() {
			System.out.println("Alpha416 ALPHA_200 Success");
			System.out.println("Disconnected from Alpha416 Multithreaded Server");
		}
		
		
		


}
