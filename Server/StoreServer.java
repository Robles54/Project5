//Author: Christopher Robles
//package application;

import java.net.*;
import java.util.HashMap;

public class StoreServer {

	private static int LISTENING_PORT = 32007;
	public static HashMap<String, Account> accounts = new HashMap<>(); 


	public static void main(String[] args) {
		ServerSocket listener;  
		Socket client;   
		if (args.length == 1) 
			LISTENING_PORT = Integer.parseInt(args[0]);
		else if (args.length > 1 )
			System.out.println("Usage:  java StoreServer <listening-port>");
		

		try {
			listener = new ServerSocket(LISTENING_PORT);
			System.out.println("Listening on port " + LISTENING_PORT);
			while (true) {
				client = listener.accept();
				StoreThread handler = new StoreThread(client);
				handler.start();
			}
		} catch (Exception e) {
			System.out.println("Sorry, the server has shut down.");
			System.out.println("Error:  " + e);
			return;
		}

	}  // end main()
} //end class
