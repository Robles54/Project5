//Author: Christopher Robles
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class AccountListScene extends SceneBasic {
    private OutputTable table = new OutputTable("Username", "Account Type");

    public AccountListScene() {
        super("Account List");

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        layout.getChildren().add(table);

        addButton("Admin Menu", e -> SceneManager.setScene(SceneManager.SceneType.admin));
        addButton("Logout", e -> logout());

        root.getChildren().add(layout);
    }

    public void getAccountList() {
        new Thread(() -> {
            try {
                Socket connection = SceneManager.getSocket(); // Server socket
                PrintWriter outgoing = new PrintWriter(connection.getOutputStream());
                BufferedReader incoming = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                System.out.println("Sending... ACCOUNT_LIST");
                outgoing.println("ACCOUNT_LIST");
                outgoing.flush();

                System.out.println("Waiting for account list...");
                
                Platform.runLater(() -> table.clearData());
                
                String username;
                while ((username = incoming.readLine()) != null && !username.equals("DONE")) {
                    String type = incoming.readLine();
                    if (type != null) {
                    	String finalUsername = username;
                    	String finalType = type;
                    	Platform.runLater(() -> {
                    		table.addRow(finalUsername, finalType);
                            System.out.println("Received: " + finalUsername + ", " + finalType);
                    	});
                    }
                }
                System.out.println("Account list retrieval completed.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }
}
