//Author: Christopher Robles
//package application;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class Account {
	private String username;
	private String password;
	private int id;
	
	//NEW id
	public int getID(){
		return id;
	}

	// Get username
	public String getUsername() {
		return username;
	}

	// Compare String to password
	public boolean verifyPassword(String password) {
		return this.password.equals(password);
	}

	// Set password
	public void setPassword(String password) {
		this.password = password;
	}

	// Get password
	public String getPassword() {
		return password;
	}
	
	public String toXML() {
	    return String.format("<account type=\"%s\" id=\"%d\"><username>%s</username><password>%s</password></account>",
	            this instanceof AdminAccount ? "admin" : "customer", getID(), getUsername(), getPassword());
	}


	// String representation of account
	@Override
	public String toString() {
		return " username: " + username + ", password: " + password + ", " + this.getClass();
	}
	
	//New getOrder method
	public synchronized void getOrder() {
		try {
			FileInputStream fileIn = new FileInputStream("orders.bin");
			DataInputStream dataIn = new DataInputStream(fileIn);
			
			while (dataIn.available() > 0) {
				int customerId = dataIn.readByte();
				int stockNumber = dataIn.readByte();
				int quantity = dataIn.readByte();
				
				if (customerId == this.id) {
					System.out.println("Order for customer " + this.id + ":");
					System.out.println("Stock Number: " + stockNumber + ", Quanity: " + quantity);
				}
			}
			
			dataIn.close();
			fileIn.close();
		} catch (IOException e) {
			System.out.println("Error: " + e);
		}
	}
	
	//New sendInventory method
	public void sendInventory(StoreThread storeThread, PrintWriter outgoing) {
		if (storeThread != null) {
			storeThread.sendInventory(outgoing);
		}
	}
	
	// Constructor

	public Account(int id, String username, String password) {
		this.username = username;
		this.password = password;
		this.id = id;
	}

}
