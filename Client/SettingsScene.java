//Author: Christopher Robles
//package application;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.application.Platform;
import javafx.geometry.Insets; 
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.*;

public class SettingsScene extends SceneBasic {
	private UserInput hostInput  = new UserInput("Host");
	private UserInput portInput = new UserInput ("Port");
	
	//private Label hostText = new Label("Host");
	//private TextField hostField = new TextField("localhost");
	//private Label portText = new Label("Port");
	//private TextField portField = new TextField("32007"); // FOR TESTING
	private Button applyButton = new Button("Apply");
	private Button cancelButton = new Button("Cancel");
	private Button chatButton = new Button("Chat");
	private Label errorMessage = new Label();

	public SettingsScene() {
        super("Connection Settings");
        
        hostInput.textField.setText("localhost");
        portInput.textField.setText("32007");
        //Creating Grid Pane 
        GridPane gridPane = new GridPane();
        gridPane.setMinSize(400, 200); 
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.TOP_CENTER);
        
        gridPane.add(hostInput, 0, 0);
        gridPane.add(portInput, 1, 0);
        
        //OLD CODE FROM PROJECT 4
//        HBox buttonBox = new HBox();
//        buttonBox.getChildren().addAll(applyButton, cancelButton, chatButton);
//        gridPane.add(buttonBox, 1, 3);
        
        errorMessage.setTextFill(Color.RED);
        gridPane.add(errorMessage, 0, 2);
        gridPane.setAlignment(Pos.TOP_CENTER);
        
        ButtonBar buttonBar = new ButtonBar();
        buttonBar.addButton("Apply", e -> apply());
        buttonBar.addButton("Cancel", e -> cancel());
        buttonBar.addButton("Chat", e -> openChat());
        gridPane.add(buttonBar, 0, 3);
        
        root.getChildren().addAll(gridPane);
        
//        applyButton.setOnAction(e -> apply());
//        cancelButton.setOnAction(e -> cancel());
//        chatButton.setOnAction(e -> openChat());
//        chatButton.setOnAction(e -> {
//        	CustomerChat chatApp = new CustomerChat();
//        	Stage chatStage = new Stage();
//        	chatApp.start(chatStage);
//        });
	}
	
	// Apply settings and attempt a connection
	private void apply() {
		String host = hostInput.getText();
		String port = portInput.getText();
		try {
			Socket connection = new Socket(host, Integer.parseInt(port));
			SceneManager.setSocket(connection); // Client socket
			System.out.println("Connection = " + connection); // For debugging
        	errorMessage.setText(""); // Clear any previous error messages
        	SceneManager.setScene(SceneManager.SceneType.login);
        }
        catch (Exception e) {
        	errorMessage.setText("Error trying to connect to server.");
            System.out.println("Error:  " + e);
        }
	}
	
	// Cancel settings change and return to login scene
	private void cancel() {
		errorMessage.setText(""); // Clear any previous error messages
		SceneManager.setScene(SceneManager.SceneType.login);
	}
	
	private void openChat() {
		String host = hostInput.getText();
		String port = portInput.getText();
		
		try {
			Socket chatSocket = new Socket(host, Integer.parseInt(port));
			
			Platform.runLater(() -> {
				CustomerChat chatApp = new CustomerChat(chatSocket);
				Stage chatStage = new Stage();
				chatApp.start(chatStage);
			});
		} catch (IOException ex) {
			System.out.println("Chat Server isn't running");
			System.out.println("Error: " + ex);
		}
	}
	
}
