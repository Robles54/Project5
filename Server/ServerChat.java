//New Maria Galarza         Project 4
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


public class ServerChat extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	private enum ConnectionState { LISTENING, CONNECTING, CONNECTED, CLOSED }

	private static String defaultPort = "32007";

	private volatile ConnectionHandler connection;

	private Button listenButton, closeButton, clearButton, quitButton, saveButton, sendButton;

	private TextField listeningPortInput, repNameInput;

	private TextField messageInput;

	private TextArea transcript;

	private Stage window;

	public void start(Stage stage) {
		window = stage;

		repNameInput = new TextField();
		repNameInput.setPromptText("Enter your name");

		closeButton = new Button("Disconnect");
		closeButton.setOnAction(this::doAction);
		closeButton.setDisable(true);
		clearButton = new Button("Clear Transcript");
		clearButton.setOnAction(this::doAction);
		sendButton = new Button("Send");
		sendButton.setOnAction(this::doAction);
		sendButton.setDisable(true);
		sendButton.setDefaultButton(true);
		saveButton = new Button("Save Transcript");
		saveButton.setOnAction(this::doAction);
		quitButton = new Button("Quit");
		quitButton.setOnAction(this::doAction);
		messageInput = new TextField();
		messageInput.setOnAction(this::doAction);
		messageInput.setEditable(false);
		transcript = new TextArea();
		transcript.setPrefRowCount(20);
		transcript.setPrefColumnCount(60);
		transcript.setWrapText(true);
		transcript.setEditable(false);
		listeningPortInput = new TextField(defaultPort);
		listeningPortInput.setPrefColumnCount(5);

		HBox buttonBar = new HBox(5, quitButton, saveButton, clearButton, closeButton);
		buttonBar.setAlignment(Pos.CENTER);
		HBox connectBar = new HBox(5, new Label("Listen on port:"), listeningPortInput, new Label("Representative Name:"), repNameInput);
		connectBar.setAlignment(Pos.CENTER);
		VBox topPane = new VBox(8, connectBar, buttonBar);
		BorderPane inputBar = new BorderPane(messageInput);
		inputBar.setLeft(new Label("Your Message:"));
		inputBar.setRight(sendButton);
		BorderPane.setMargin(messageInput, new Insets(0, 5, 0, 5));

		BorderPane root = new BorderPane(transcript);
		root.setTop(topPane);
		root.setBottom(inputBar);
		root.setStyle("-fx-border-color: #444; -fx-border-width: 3px");
		inputBar.setStyle("-fx-padding:5px; -fx-border-color: #444; -fx-border-width: 3px 0 0 0");
		topPane.setStyle("-fx-padding:5px; -fx-border-color: #444; -fx-border-width: 0 0 3px 0");

		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Chat Server");
		stage.setOnHidden(e -> {
			if (connection != null)
				connection.close();
		});
		stage.show();
	}
	
	   
    /**
     * A little wrapper for showing an error alert.
     */
    private void errorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.showAndWait();
    }

    /**
     * Defines responses to buttons.  (In this program, I use one
     * method to handle all the buttons; the source of the event
     * can be used to determine which button was clicked.)
     */
    private void doAction(ActionEvent evt) {
        Object source = evt.getSource();
        if (source == listenButton) {
            if (connection == null || 
                    connection.getConnectionState() == ConnectionState.CLOSED) {
                String portString = listeningPortInput.getText();
                int port;
                try {
                    port = Integer.parseInt(portString);
                    if (port < 0 || port > 65535)
                        throw new NumberFormatException();
                }
                catch (NumberFormatException e) {
                    errorMessage(portString + "is not a legal port number.");
                    return;
                }

                listenButton.setDisable(true);
                closeButton.setDisable(false);
                connection = new ConnectionHandler(port);
            }
        }
        else if (source == closeButton) {
            if (connection != null)
                connection.close();
        }
        else if (source == clearButton) {
            transcript.setText("");
        }
        else if (source == quitButton) {
            try {
                window.hide();
            }
            catch (SecurityException e) {
            }
        }
        else if (source == saveButton) {
            doSave();
        }
        else if (source == sendButton || source == messageInput) {
            if (connection != null && 
                    connection.getConnectionState() == ConnectionState.CONNECTED) {
                connection.send(messageInput.getText());
                messageInput.selectAll();
                messageInput.requestFocus();
            }
        }
    }
   
    private void doSave() {
        FileChooser fileDialog = new FileChooser(); 
        fileDialog.setInitialFileName("transcript.txt");
        fileDialog.setInitialDirectory(new File(System.getProperty("user.home")));
        fileDialog.setTitle("Select File to be Saved");
        File selectedFile = fileDialog.showSaveDialog(window);
        if (selectedFile == null)
            return; 
        PrintWriter out; 
        try {
            FileWriter stream = new FileWriter(selectedFile); 
            out = new PrintWriter( stream );
        }
        catch (Exception e) {
            errorMessage("Sorry, but an error occurred while\ntrying to open the file:\n" + e);
            return;
        }
        try {
            out.print(transcript.getText());
            out.close();
            if (out.checkError()) 
                throw new IOException("Error check failed.");
        }
        catch (Exception e) {
            errorMessage("Sorry, but an error occurred while\ntrying to write the text:\n" + e);
        }    
    }


    /**
     * Add a line of text to the transcript area.
     * @param message text to be added; a line feed is added at the end
     */
    private void postMessage(String message) {
        Platform.runLater( () -> transcript.appendText(message + '\n') );
    }

    private class ConnectionHandler extends Thread {

        private volatile ConnectionState state;
        private String remoteHost;
        private int port;
        private ServerSocket listener;
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        ConnectionHandler(int port) {
            state = ConnectionState.LISTENING;
            this.port = port;
            postMessage("\nLISTENING ON PORT " + port + "\n");
            try { setDaemon(true); }
            catch (Exception e) {}
            start();
        }

        ConnectionHandler(String remoteHost, int port) {
            state = ConnectionState.CONNECTING;
            this.remoteHost = remoteHost;
            this.port = port;
            postMessage("\nCONNECTING TO " + remoteHost + " ON PORT " + port + "\n");
            try { setDaemon(true); }
            catch (Exception e) {}
            start();
        }

        synchronized ConnectionState getConnectionState() {
            return state;
        }

        synchronized void send(String message) {
            if (state == ConnectionState.CONNECTED) {
                postMessage("SEND:  " + message);
                out.println(message);
                out.flush();
                if (out.checkError()) {
                    postMessage("\nERROR OCCURRED WHILE TRYING TO SEND DATA.");
                    close();
                }
            }
        }

        synchronized void close() {
            state = ConnectionState.CLOSED;
            try {
                if (socket != null)
                    socket.close();
                else if (listener != null)
                    listener.close();
            }
            catch (IOException e) {
            }
        }

        synchronized private void received(String message) {
            if (state == ConnectionState.CONNECTED)
                postMessage("RECEIVE:  " + message);
            	out.println(message);
            	out.flush();
        }

        synchronized private void connectionOpened() throws IOException {
            listener = null;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            state = ConnectionState.CONNECTED;
            Platform.runLater( () -> { 
                closeButton.setDisable(false);
                sendButton.setDisable(false);
                messageInput.setEditable(true);
                messageInput.setText("");
                messageInput.requestFocus();
                postMessage("CONNECTION ESTABLISHED\n");
            });
        }

        synchronized private void connectionClosedFromOtherSide() {
            if (state == ConnectionState.CONNECTED) {
                postMessage("\nCONNECTION CLOSED FROM OTHER SIDE\n");
                state = ConnectionState.CLOSED;
            }
        }


        private void cleanUp() {
            state = ConnectionState.CLOSED;
            Platform.runLater( () -> {
                listenButton.setDisable(false);
                closeButton.setDisable(true);
                sendButton.setDisable(true);
                messageInput.setEditable(false);
                postMessage("\n*** CONNECTION CLOSED ***\n");
            });
            if (socket != null && !socket.isClosed()) {
                // Make sure that the socket, if any, is closed.
                try {
                    socket.close();
                }
                catch (IOException e) {
                }
            }
            socket = null;
            in = null;
            out = null;
            listener = null;
        }
        
        public void run() {
            try {
                if (state == ConnectionState.LISTENING) {
                    listener = new ServerSocket(port);
                    System.out.println("Server Started and listening on Port: " + port);
                    socket = listener.accept();
                    System.out.println("Client is connected: " + socket.getRemoteSocketAddress());
                    listener.close();
                }
                else if (state == ConnectionState.CONNECTING) {
                    socket = new Socket(remoteHost,port);
                }
                connectionOpened();
                while (state == ConnectionState.CONNECTED) {
                    String input = in.readLine();
                    if (input == null)
                        connectionClosedFromOtherSide();
                    else
                    	System.out.println("Received message: " + input);
                        received(input);
                }
            }
            catch (Exception e) {

                if (state != ConnectionState.CLOSED)
                    postMessage("\n\n ERROR:  " + e);
            }
            finally {
                cleanUp();
            }
        }
    }
}


