//Author: Christopher Robles
//package application;

import javafx.scene.control.*;

public class CustomerScene extends SceneBasic {
	

	public CustomerScene() {
        super("Customer Menu");
        int WIDTH = 200;
        
        addButton("View Order", e -> SceneManager.setScene(SceneManager.SceneType.viewOrders));
        addButton("Place Order", e -> SceneManager.setScene(SceneManager.SceneType.placeOrder));
        addButton("Show Profile", e -> SceneManager.setScene(SceneManager.SceneType.profile));
        addButton("Change Password", e -> SceneManager.setScene(SceneManager.SceneType.changePassword));
        //WAS NEVER ABLE TO FIGURE OUT THE CHAT
        //addButton("Chat", e -> SceneManager.setScene(SceneManager.SceneType.customerChat));
        addButton("Logout", e -> logout());
       
        
        //OLD CODE FROM PROJECT 4
//        viewOrderButton.setMinWidth(WIDTH);
//        placeOrderButton.setMinWidth(WIDTH);
//        profileButton.setMinWidth(WIDTH);
//        passwordButton.setMinWidth(WIDTH);
//        logoutButton.setMinWidth(WIDTH);
//        chatButton.setMinWidth(WIDTH);
//        root.getChildren().addAll(viewOrderButton);
//        viewOrderButton.setOnAction(e -> SceneManager.setScene(SceneManager.SceneType.viewOrders));
//        root.getChildren().addAll(placeOrderButton);
//        placeOrderButton.setOnAction(e -> SceneManager.setScene(SceneManager.SceneType.placeOrder));    
//        root.getChildren().addAll(profileButton);
//        profileButton.setOnAction(e -> SceneManager.setScene(SceneManager.SceneType.profile));
//        root.getChildren().addAll(passwordButton);
//        passwordButton.setOnAction(e -> SceneManager.setScene(SceneManager.SceneType.changePassword));
//        root.getChildren().addAll(logoutButton);
//        logoutButton.setOnAction(e -> logout());
//        root.getChildren().addAll(chatButton);
       // chatButton.setOnAction(e -> chat());
	}
}
