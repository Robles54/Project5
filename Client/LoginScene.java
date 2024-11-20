//Modify Maria Galarza          Project 4
//Form for user login. NEW: Add “Chat” button.
//package application;

import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.application.Platform;
import javafx.geometry.Insets; 

import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.*;
import java.io.*;

public class LoginScene extends SceneBasic {
	private UserInput username = new UserInput("Username");
	private PasswordInput password = new PasswordInput("Password");
	//private Label userText = new Label("Username");
	//private TextField userField = new TextField("admin");
	//private Label passText = new Label("Password");
//	private PasswordField passField = new PasswordField();
	//private TextField passField = new TextField("password");
	private Label errorMessage = new Label();
	private Socket connection;
	private String hostName = "127.0.0.1";
	private int LISTENING_PORT = 32007;

	public LoginScene() {
        super("Login Menu");
        //Creating Grid Pane 
        GridPane gridPane = new GridPane();
        gridPane.setMinSize(400, 200); 
        gridPane.setPadding(new Insets(10, 10, 10, 10)); 
        gridPane.setVgap(5); 
        gridPane.setHgap(5);       
        gridPane.add(username, 0, 0);
        gridPane.add(password, 0, 1);
        
        ButtonBar buttonBar = new ButtonBar();
        buttonBar.addButton("Log In", e -> login());
        buttonBar.addButton("Settings", e ->SceneManager.setScene(SceneManager.SceneType.settings));
        //buttonBar.addButton("Chat", e -> openChat());
        gridPane.add(buttonBar, 0, 2);
        //OLD CODE FROM PROJECT 4
//        HBox buttonBox = new HBox();
//        buttonBox.getChildren().addAll(loginButton, settingsButton, chatButton);
//        gridPane.add(buttonBox, 1, 2);
        errorMessage.setTextFill(Color.RED);
        gridPane.add(errorMessage, 1, 3);
        gridPane.setAlignment(Pos.TOP_CENTER);
        root.getChildren().addAll(gridPane);
		
//		chatButton.setOnAction(e -> {
//		    try {
//		        // Create a new Socket for the chat connection
//		        Socket chatSocket = new Socket("localhost", 32007); 
//		        Platform.runLater(() -> {
//                    errorMessage.setText("");
//                    chatButton.setDisable(false);
//                    // Create and set ChatScene or handle chat logic here
//                });
//		        
//		    } catch (IOException ex) {
//		    	Platform.runLater(() -> {
//                    errorMessage.setText("Chat server is not running.");
//                    chatButton.setDisable(false);
//                });
//		        System.out.println("Error connecting to chat server: " + ex.getMessage());
//		    }
//		});
		
//		chatButton.setOnAction(e -> {
//			CustomerChat chatApp = new CustomerChat();
//			Stage chatStage = new Stage();
//			chatApp.start(chatStage);
//		});
		
		
		
	}
	
	private void login() {
		connection = SceneManager.getSocket(); 
		try {
			if (connection == null) { 
				connection = new Socket(hostName, LISTENING_PORT);
				SceneManager.setSocket(connection);
			}
		}
	    catch (Exception e) {
	        System.out.println("Error:  " + e);
	    }

		String typedInUsername = username.getText();
		String typedInPassword = password.getText();
       
		try {
        	PrintWriter outgoing; 
			outgoing = new PrintWriter( connection.getOutputStream() );
			outgoing.println("LOGIN");
			outgoing.println(typedInUsername);
			outgoing.println(typedInPassword);
			outgoing.flush(); 

            BufferedReader incoming = new BufferedReader( 
                    new InputStreamReader(connection.getInputStream()) );
            System.out.println("Waiting for account type...");
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
        	errorMessage.setText("Error trying to connect to server.");
            System.out.println("Error:  " + e);
        }
	}
	
	private void openChat() {
		//Not sure how to implement the chat from LoginScene - Chris
	}
	
	// Used to set as current scene
	public Scene getScene() {
		errorMessage.setText("");
        return this.scene;
	}
}
