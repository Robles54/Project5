// From  Project #3
//package application;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane; 
import javafx.geometry.Insets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javafx.scene.paint.Color;

public class ChangePasswordScene extends SceneBasic {
	private PasswordInput oldPassword = new PasswordInput("Old Password");
	private PasswordInput newPassword = new PasswordInput("New Password");
	//private Label oldText = new Label("Old password");
	//private Label newText = new Label("New password");
	private Label errorMessage = new Label();

	public ChangePasswordScene() {
        super("Change Password");
        //Creating Grid Pane 
		errorMessage.setTextFill(Color.RED);
        GridPane gridPane = new GridPane();
        gridPane.setMinSize(400, 200); 
        gridPane.setPadding(new Insets(10, 10, 10, 10)); 
        gridPane.setVgap(5); 
        gridPane.setHgap(5); 
        addButton("Change Password", e -> change());
        addButton("Return To Menu", e -> SceneManager.setScene(SceneManager.SceneType.customer));
        //gridPane.add(oldText, 0, 0);
        gridPane.add(oldPassword, 1, 0);
        //gridPane.add(newText, 0,1);
        gridPane.add(newPassword, 1, 1);
//        gridPane.add(button, 1, 2);
        gridPane.add(errorMessage, 1, 3);
        gridPane.setAlignment(Pos.TOP_CENTER);
        root.getChildren().addAll(gridPane);
	}
	
	// Action to change password by sending a signal, password info, and expecting an account type
	private void change() {
		try {
			Socket connection = SceneManager.getSocket(); // Server socket
	    	PrintWriter outgoing;   // Stream for sending data.
			outgoing = new PrintWriter( connection.getOutputStream() );
			System.out.println("Sending... CHANGE_PASSWORD");
			outgoing.println("CHANGE_PASSWORD");
			outgoing.flush();
	
			String oldInput = oldPassword.getText();
			String newInput = newPassword.getText();
			
			outgoing.println(oldInput);
			outgoing.println(newInput);
			outgoing.flush();  // Make sure the data is actually sent!
            System.out.println("Sent password info"); // For debugging

            BufferedReader incoming = new BufferedReader( 
                    new InputStreamReader(connection.getInputStream()) );
            System.out.println("Waiting for result...");
            String reply = incoming.readLine();
            if (reply.equals("ADMIN")) {
            	errorMessage.setText("");
            	SceneManager.setScene(SceneManager.SceneType.admin);
            }
            else if (reply.equals("CLIENT")) {
            	errorMessage.setText("");
            	SceneManager.setScene(SceneManager.SceneType.customer);
            }
            else
            	errorMessage.setText(reply);
		}
        catch (Exception e) {
            System.out.println("Error:  " + e);
        }
	}
}
