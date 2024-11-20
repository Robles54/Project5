// Author: Christopher Robles
//package application;

import javafx.application.Application;

import javafx.stage.Stage;


import java.io.PrintWriter;
import java.net.Socket;

public class Store extends Application {
	private SceneManager sceneManager;
	private Socket connection; 

	// Start GUI
    public void start(Stage stage) {
    	sceneManager = new SceneManager();
    	sceneManager.setStage(stage);
    	SceneManager.setScene(SceneManager.SceneType.login);
        stage.setTitle("Store Application");
        stage.show();
    } // end start();
    

    public void stop() {
    	try {
            System.out.println("Quitting");
        	Socket setConnection = sceneManager.getSocket();
        	
        	if (setConnection == null) {
        		System.out.println("Error: No active socket. Cannot send QUIT.");
        		SceneManager.setScene(SceneManager.SceneType.login);
        		return;
        	}
	    	PrintWriter outgoing;  
			outgoing = new PrintWriter( setConnection.getOutputStream() );
			outgoing.println("QUIT");
			outgoing.flush();
			
			setConnection.close();
			SceneManager.setSocket(null);
			
			SceneManager.setScene(SceneManager.SceneType.login);
    	}
        catch (Exception e) {
            System.out.println("Error:  " + e);
        }
    }

    public static void main(String[] args) {
        launch(args);  // Run this Application.
    }
}
