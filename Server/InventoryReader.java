// New Chris           Project 4 
// Utility program for reading XML file with inventory data
//package application;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import java.io.*;

public class InventoryReader {
	public static HashMap<String, String> readFile(String filename){
		HashMap<String, String> inventory = new HashMap<>();

		try (Scanner scanner = new Scanner(new File(filename)))  {

			String stockNumber = null;
			String description = "";

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();

				if (line.equals("<PRODUCT>")) {
					while (!line.equals("</PRODUCT>")) {
						line = scanner.nextLine().trim();
						if (line.equals("<stockNumber>")) {
							stockNumber = scanner.nextLine().trim();
						} else if (line.equals("<description>")) {
							description = scanner.nextLine().trim();
						}
					}
					if (stockNumber != null && description != null) {
						inventory.put(stockNumber, description);
						stockNumber = null;
						description = null;
					}
				}
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("Can not find inventory: " + e.getMessage());
		}

		return inventory;

	}

}