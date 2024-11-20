//New Christopher Robles         Project 4
//package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

import java.io.*;
import java.net.*;


public class CustomerChat extends Application {

//	private enum ConnectionState { LISTENING, CONNECTING, CONNECTED, CLOSED }
	
	private Button closeButton, sendButton;
	private TextField listeningPortInput, custNameInput;
	private TextField messageInput;
	private TextArea transcript;
	private Socket chatSocket;
	private PrintWriter out;
	private BufferedReader in;
	
	private static String SERVER_HOST = "localhost";
	private static final int SERVER_PORT = 32007;

	//private Stage window;
	
	public CustomerChat (Socket chatSocket) {
		this.chatSocket = chatSocket;
	}

	public void start(Stage stage) {
		
		stage.setTitle("Customer Chat");
		//custNameInput = new TextField();
		//custNameInput.setPromptText("Enter your name");
		transcript = new TextArea();
		transcript.setPrefRowCount(20);
		transcript.setPrefColumnCount(60);
		transcript.setWrapText(true);
		transcript.setEditable(false);
		
		messageInput = new TextField();
		messageInput.setPromptText("Type your message...");
		messageInput.setOnAction(e -> sendMessage());
		
		sendButton = new Button("Send");
		sendButton.setOnAction(e -> sendMessage());
		
		closeButton = new Button("Close");
		closeButton.setOnAction(e -> closeChat());
		
//		listeningPortInput = new TextField(defaultPort);
//		listeningPortInput.setPrefColumnCount(5);
		HBox buttonBar = new HBox(10, sendButton, closeButton);
		buttonBar.setAlignment(Pos.CENTER);
		
//		HBox connectBar = new HBox(5, new Label("Listen on port:"), listeningPortInput, new Label("Representative Name:"), custNameInput);
//		connectBar.setAlignment(Pos.CENTER);
		
		VBox vbox = new VBox(10, transcript, messageInput, buttonBar);
		vbox.setPadding(new Insets(10));
		//BorderPane inputBar = new BorderPane(messageInput);

//		BorderPane root = new BorderPane(transcript);
//		root.setTop(topPane);
//		root.setBottom(inputBar);
//		root.setStyle("-fx-border-color: #444; -fx-border-width: 3px");
//		inputBar.setStyle("-fx-padding:5px; -fx-border-color: #444; -fx-border-width: 3px 0 0 0");
//		topPane.setStyle("-fx-padding:5px; -fx-border-color: #444; -fx-border-width: 0 0 3px 0");

		Scene scene = new Scene(vbox);
		stage.setScene(scene);
		stage.show();
		
//		stage.setTitle("Chat Server");
//		stage.setOnHidden(e -> {
//			if (connection != null)
//				connection.close();
//		});
//		stage.show();
		startChatConnection();
	}
	
	   
    /**
     * A little wrapper for showing an error alert.
     */
//    private void errorMessage(String message) {
//        Alert alert = new Alert(Alert.AlertType.ERROR, message);
//        alert.showAndWait();
//    }
   
	private void startChatConnection() {
        try {
            out = new PrintWriter(chatSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(chatSocket.getInputStream()));

            out.println("Customer connected: " + "Customer_" + System.currentTimeMillis());

            new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        postMessage(message); 
                    }
                } catch (IOException e) {
                    System.out.println("Error reading from server: " + e.getMessage());
                }
            }).start();

        } catch (IOException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
            postMessage("Failed to connect to the server.");
        }
    }

   private void sendMessage() {
	   String message = messageInput.getText();
	   if (!message.isEmpty()) {
		   postMessage ("You: " + message);
		   out.println(message);
           out.flush();
		   messageInput.clear();
	   }
   }
    
    private void postMessage(String message) {
        Platform.runLater(() -> {
        	transcript.appendText(message + "\n");
        	transcript.setScrollTop(Double.MAX_VALUE);
        });
    }
    
    private void closeChat() {
    	try {
    		if (chatSocket != null && !chatSocket.isClosed()) {
    			chatSocket.close();
    		}
    	} catch (IOException e) {
    		System.out.println("Error: " + e);
    	}
    	Platform.runLater(() -> {
    		System.exit(0);
    	});
    }
    
    public static void main(String[] args) {
		launch(args);
	}
}


