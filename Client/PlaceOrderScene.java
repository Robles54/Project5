import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PlaceOrderScene extends SceneBasic {
    private UserInput stockNumberField = new UserInput("Stock Number");
    private UserInput quantityField = new UserInput("Quantity");
    private OutputTable table = new OutputTable("Stock #", "Description", "Quantity");
    private Label errorMessage = new Label();

    public PlaceOrderScene() {
        super("Place Order");

        stockNumberField.textField.setText("0");
        quantityField.textField.setText("0");

        errorMessage.setTextFill(Color.RED);

        VBox layout = new VBox(10, stockNumberField, quantityField, table, errorMessage);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        addButton("Submit Order", e -> sendOrder());
        addButton("Return to Menu", e -> SceneManager.setScene(SceneManager.SceneType.customer));

        root.getChildren().add(layout);
    }

    public void getInventory() {
        new Thread(() -> {
            try {
                Socket connection = SceneManager.getSocket();
                BufferedReader incoming = SceneManager.getIncoming();
                PrintWriter outgoing = SceneManager.getOutgoing();

                System.out.println("Sending... VIEW_INVENTORY");
                outgoing.println("VIEW_INVENTORY");
                outgoing.flush();

                System.out.println("Waiting for inventory...");
                Platform.runLater(() -> table.clearData());

                String line;
                while ((line = incoming.readLine()) != null) {
                    System.out.println("Received Line: " + line.trim());
                    if (line.trim().equalsIgnoreCase("DONE")) {
                        System.out.println("Inventory data transmission completed.");
                        break;
                    }

                    String[] inventoryData = line.split(",");
                    System.out.println("Split Data Length: " + inventoryData.length);

                    if (inventoryData.length < 2) {
                        System.out.println("Skipping invalid line: " + line);
                        continue;
                    }

                    String stockNumber = inventoryData[0];
                    String description = inventoryData[1];
                    String quantity = (inventoryData.length == 3) ? inventoryData[2] : "0";

                    Platform.runLater(() -> table.addRow(stockNumber, description, quantity));
                }
            } catch (Exception e) {
                System.out.println("Error fetching inventory: " + e);
                e.printStackTrace();
            }
        }).start();
    }


    public void sendOrder() {
        String stockNumber = stockNumberField.getText().trim();
        String quantity = quantityField.getText().trim();

        if (stockNumber.isEmpty() || quantity.isEmpty()) {
            errorMessage.setText("Please fill in both Stock Number and Quantity.");
            return;
        }

        new Thread(() -> {
            try {
                Socket connection = SceneManager.getSocket();
                BufferedReader incoming = SceneManager.getIncoming();
                PrintWriter outgoing = SceneManager.getOutgoing();

                System.out.println("Sending Order...");
                outgoing.println("PLACE_ORDER");
                outgoing.println(stockNumber + "," + quantity);
                outgoing.flush();

                String response = incoming.readLine();
                Platform.runLater(() -> {
                    if ("ORDER_PLACED".equals(response)) {
                        errorMessage.setTextFill(Color.GREEN);
                        errorMessage.setText("Order placed successfully!");
                    } else {
                        errorMessage.setTextFill(Color.RED);
                        errorMessage.setText("Error placing order. Please try again.");
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> errorMessage.setText("Error sending order: " + e.getMessage()));
                System.out.println("Error sending order: " + e);
            }
        }).start();
    }
}
