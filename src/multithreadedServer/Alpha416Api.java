package multithreadedServer;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class Alpha416Api {
	
	private final String APIKEY = "J8IHBXGBZZIHEE6D";
	private String requestURL;
	private HttpURLConnection connection;
	
	
	// for exchange
	public String getConnectExchange(String country1, String country2) throws URISyntaxException, IOException {
		//this.requestURL = "https://www.alphavantage.co/query?function=CURRENCY_EXCHANGE_RATE&from_currency=" + country1.toUpperCase() + "&to_currency=" + country2.toUpperCase() + "&apikey=" + this.APIKEY;
		
		// Used to test the API
		this.requestURL = "https://www.alphavantage.co/query?function=CURRENCY_EXCHANGE_RATE&from_currency=USD&to_currency=JPY&apikey=demo";
		return this.getData(requestURL, "GET");
	}
	
	// for Gas
	public String getConnectGas() throws URISyntaxException, IOException {
		this.requestURL = "https://www.alphavantage.co/query?function=NATURAL_GAS&interval=daily&apikey=" + this.APIKEY;
		
		// Used to test the API
		//this.requestURL = "https://www.alphavantage.co/query?function=NATURAL_GAS&interval=monthly&apikey=demo";
		
		return this.getData(this.requestURL, "GET");
	}
	
	// getting data from the API
	public String getData(String requestURL, String requestMethod) throws URISyntaxException, IOException {
		URI uri = new URI(requestURL);
		URL url = uri.toURL();
		this.connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(requestMethod);
		connection.connect();
		
		int responseCode = connection.getResponseCode();
	    if (responseCode != HttpURLConnection.HTTP_OK) {
	        return "Alpha416 ALPHA" + responseCode;
	    }
	    
		String inline = "";
		try (Scanner scanner = new Scanner(url.openStream())) {
	        while (scanner.hasNext()) {
	            inline += scanner.nextLine();
	        }
	        scanner.close();
	    }
		
		return inline;
	}
	
	public String parseExchange(String message) {
		JSONObject json = new JSONObject(message);
		
		if (json.has("Realtime Currency Exchange Rate")) {
			
	        json = json.getJSONObject("Realtime Currency Exchange Rate");
	        return json.getString("5. Exchange Rate");
	        
	    } 
		else if (json.has("Information")) {
	    	
	        return "Alpha416 ALPHA_500 Server Fault\n" + json.getString("Information");
	        
	    } 
		else {
	        return "Alpha416 ALPHA_400 Invalid Request\nError: Invalid parameters for EXC command.";
	    }
		

	}
	
	public  String parseRefreshed(String message) {
		JSONObject json = new JSONObject(message);
		
		if (json.has("Realtime Currency Exchange Rate")) {
	        json = json.getJSONObject("Realtime Currency Exchange Rate");
	        return json.getString("6. Last Refreshed");
	    } 
		
		else if (json.has("Information")) {
	        return "Alpha416 ALPHA_500 Server Fault\n" + json.getString("Information");
	    } 
		
		else {
	        return "Alpha416 ALPHA_400 Invalid Request\nError: Invalid parameters for EXC command.";
	    }
	}
	
	
	public String parsePrice(String message) {
		JSONObject json = new JSONObject(message);
		if (json.has("Information")) {
			return "Alpha416 ALPHA_500 Server Fault\n" + json.getString("Information");
		}
		else {
			JSONArray jsonArray = json.getJSONArray("data");
			return jsonArray.getJSONObject(0).getString("value");
		}
	}
	
	// find date and return the price in double
	public Double findDate(String message, String date) {
		JSONObject json = new JSONObject(message);
		JSONArray jsonArray = json.getJSONArray("data");
		for(int i = 0; i <jsonArray.length(); i++) {
			if (jsonArray.getJSONObject(i).getString("date").compareTo(date)==0) {
				String response = jsonArray.getJSONObject(i).getString("value");
				return Double.parseDouble(response);
			}
		}
		return null;
	}
	
	public String change(String message, String date1, String date2) {
		if(findDate(message, date2) - findDate(message, date1) > 0) {
			return "Price Increased between " + date1 + " " + date2;
		}
		else {
			return "Price Decreased between " + date1 + " " + date2;
		}
	}
	
	public Double average(String message, String date1, String date2) {
		return (findDate(message, date2) + findDate(message, date1))/2;
	}

	

}
