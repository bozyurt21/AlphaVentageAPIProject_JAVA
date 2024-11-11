package multithreadedClient;

import java.util.Scanner;
import java.io.IOException;

public class ClientMultithread {
	public static void main(String[] args) {
		try {
			Client client = new Client();
			Scanner scanner = new Scanner(System.in);
			try {
		        client.connect();
		        System.out.println("Enter 'Exc' to exchange rate between two currencies.");
		        System.out.println("Enter 'Gas' to get natural gas data for a specific date.");
		        System.out.println("Enter 'Quit' to disconnect from the server.");
		        
		        String message = scanner.nextLine();
		        
		        String code1, code2, date1, date2;
		        
		        boolean average, refresh;
		        
		        while (message.compareToIgnoreCase("quit")!=0) { // while client did not quit
		        	
		            if (message.compareToIgnoreCase("exc") ==0) { // if client wants to exchange
		            	
		            	System.out.println("Enter the first country code: "); // getting the first country code from the client
		            	code1 = scanner.nextLine();
		            	System.out.println("Enter the second country code: "); // getting the second country code from the client
		            	code2 = scanner.nextLine();
		            	System.out.println("Do you want the refresh date? ('y' for yes and 'n' for no): "); // is it going to be refreshed?
		            	if (scanner.nextLine().compareToIgnoreCase("y") == 0) {
		            		refresh = true;
		            	}
		            	else {
		            		refresh = false;
		            	}
		            	//TODO: send exchange request to server.
		            	client.sendExchangeRequest(message, code1, code2, refresh);
		            	
		            }
		            
		            else if (message.compareToIgnoreCase("gas") == 0) { 
		            	System.out.println("Enter the first date: (write as yy-mm-dd)");
		            	date1 = scanner.nextLine();
		            	System.out.println("Do you want to see the change in price between two dates?: ('y' for yes 'n' for no)");
		            	if (scanner.nextLine().compareToIgnoreCase("y") == 0) {
		            		System.out.println("Enter the second date: (write as yy-mm-dd):");
			            	date2 = scanner.nextLine();
		            	}
		            	else {
		            		date2 = null;
		            	}
		            	System.out.println("Do you want average? ('y' for yes and 'n' for no): ");
		            	System.out.println("If you want to see the price change then please do not say yes!");
		            	if (scanner.nextLine().compareToIgnoreCase("y") == 0 && date2 != null) {
		            		
		            		average = true;
		            	}
		            	else {
		            		average = false;
		            	}
		            	
		            	//TODO: send gas request to server.
		            	client.sendGasRequest(message, date1, date2, average);
		            	
		            }
		            else {
		            	System.out.println("Please enter valid response!");
		            }
		            
		            client.recieveResonse();
		            
		            System.out.println("Enter 'Exchange' to exchange rate between two currencies.");
			        System.out.println("Enter 'Gas' to get natural gas data for a specific date.");
			        System.out.println("Enter 'Quit' to disconnect from the server.");
			        
			        message = scanner.nextLine();
		        }
		       client.disconnect();
	        } 
		    catch(IOException e) {
	        	System.err.println(e.getMessage());
	        } finally {
				scanner.close();
	        }
		}
		catch (IOException e) {
			System.out.println("Has some problem through connection. Please see the below error message: ");
			e.printStackTrace();
		}
		
	}
}
