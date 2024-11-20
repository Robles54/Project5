//New Maria Galarza         Project 4
//package application;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.io.File;
import java.util.Scanner;

public class AccountsReader {
    public static HashMap<String, Account> readFile(String filename) {
    	Scanner data;
    	HashMap<String, Account> accounts = new HashMap<>();

    	try {
            data = new Scanner(new File(filename));
        }
        catch (FileNotFoundException e) {
            System.out.println("Can't find file!");
            return null;
        }
    	Account a = null;
        while ( data.hasNextLine() ) { 
            int id = 0;
            String username = "";
            String password = "";
            String profile = "";
            String line = data.nextLine().trim();
            
            if (line.equals("<CUSTOMER>")) {
                while (!line.equals("</CUSTOMER>")) {
                    line = data.nextLine().trim();
                    if (line.equals("<id>")) {
                        id = Integer.parseInt(data.nextLine().trim());
                    } else if (line.equals("<username>")) {
                        username = data.nextLine().trim();
                    } else if (line.equals("<password>")) {
                        password = data.nextLine().trim();
                    } 
                }
                a = new CustomerAccount(id, username, password, profile);
                accounts.put(username, a);
            }
            else if (line.equals("<ADMINISTRATOR>")) {
                while (!line.equals("</ADMINISTRATOR>")) {
                    line = data.nextLine().trim();
                    if (line.equals("<id>")) {
                        id = Integer.parseInt(data.nextLine().trim());
                    } else if (line.equals("<username>")) {
                        username = data.nextLine().trim();
                    } else if (line.equals("<password>")) {
                        password = data.nextLine().trim();
                    } 
                }
                a = new AdminAccount(id, username, password);
                accounts.put(username, a);
            }
        }
        data.close();
        return accounts;
    }  // end of readFile()
    
    public static void writeFile(HashMap<String, Account> accounts, String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            writer.write("<accounts>\n");
            for (Account account : accounts.values()) {
                writer.write(account.toXML() + "\n");
            }
            writer.write("</accounts>");
            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing accounts to XML: " + e.getMessage());
        }
    }

    
} // end class AccountsReader

