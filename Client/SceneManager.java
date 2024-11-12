/* Author: Chris Fietkiewicz. For Project #4.
   Description: Manages changes from one scene to another scene.
   Usage: The setScene() method should be called using a SceneType enumerated constant. Examples:
   
   SceneManager.setScene(SceneManager.SceneType.login);
   SceneManager.setScene(SceneManager.SceneType.admin);
   SceneManager.setScene(SceneManager.SceneType.customer);
   SceneManager.setScene(SceneManager.SceneType.changePassword);
   SceneManager.setScene(SceneManager.SceneType.accountList);
   SceneManager.setScene(SceneManager.SceneType.profile);
   SceneManager.setScene(SceneManager.SceneType.settings);
   SceneManager.setScene(SceneManager.SceneType.placeOrder);
   SceneManager.setScene(SceneManager.SceneType.viewOrders);
*/

import java.net.Socket;
import java.util.HashMap;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class SceneManager {
	public static enum SceneType {login, admin, customer, changePassword, accountList, profile, settings, placeOrder, viewOrders} // Custom data type for scene selection
	private static HashMap<SceneType, SceneBasic> scenes = new HashMap<SceneType, SceneBasic>(); // Lookup table for retrieving scene objects
    private static Socket connection; // Socket connection to server
	private static Stage stage; // Stage used for all scenes

	// Constructor
	public SceneManager() {
		scenes.put(SceneType.login, new LoginScene());
		scenes.put(SceneType.admin, new AdminScene());
		scenes.put(SceneType.customer, new CustomerScene());
		scenes.put(SceneType.changePassword, new ChangePasswordScene());
		scenes.put(SceneType.accountList, new AccountListScene());
		scenes.put(SceneType.profile, new ProfileScene());
		scenes.put(SceneType.settings, new SettingsScene());
		scenes.put(SceneType.placeOrder, new PlaceOrderScene());
		scenes.put(SceneType.viewOrders, new ViewOrdersScene());
	}
	
	// Set socket connection to server
	public static void setSocket(Socket setConnection) {
		connection = setConnection;
	}

	// Get socket connection to server
	public static Socket getSocket() {
		return connection;
	}

	// Set initial stage to be used by all scenes
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	// Change view to selected scene
	public static void setScene(SceneType type) {
		if (type == SceneType.accountList)
			((AccountListScene) scenes.get(type)).getAccountList(); // Make AccountListScene request account list from server
		else if (type == SceneType.profile)
			((ProfileScene) scenes.get(type)).getProfile(); // Make AccountListScene request account list from server
		else if (type == SceneType.placeOrder)
			((PlaceOrderScene) scenes.get(type)).getInventory(); // Make placeOrderScene request inventory list from server
		else if (type == SceneType.viewOrders)
			((ViewOrdersScene) scenes.get(type)).getOrders(); // Make placeOrderScene request inventory list from server
		stage.setScene(scenes.get(type).getScene()); // Switch to the selected scene
	}
}
