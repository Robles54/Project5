// Author: Chris Fietkiewicz. 
// Provided as a demonstration for required new classes.
// It can be compiled with Store.java to check for correct
// method names and parameters. It should compile without errors.
// NOTE: It is *not* intended to be executed.

public class DemoScene extends SceneBasic{
	private OutputTable table = new OutputTable("Column #0", "Column #1"); // Create table with 2 columns
	private UserInput input = new UserInput("Username "); // Create Label and TextField
	private PasswordInput password = new PasswordInput("Password "); // Create Label and PasswordField

	public DemoScene() {
		super("Demonstration of using new classes");
        addButton("Do something", e -> demoMethod("String #0")); // Create Button in VBox from SceneBasic
        ButtonBar buttons = new ButtonBar(); // Create HBox for a button bar
        buttons.addButton("Do something in a button bar", e -> demoMethod("String #0", "String #1")); // Creates a button in the HBox from ButtonBar
		root.getChildren().addAll(table, input, password, buttons); // Create other controls in the VBox after the 1st button
	}

	// Example of a method that is called when a button is clicked
	public void demoMethod(String... stringArray) {
	    String s = input.getText(); // Returns String in the TextField
	    String p = password.getText(); // Returns String in the PasswordField
	    if (stringArray.length >= 2) // If method received at least 2 parameters, add them to the table
	    	table.addRow(stringArray[0], stringArray[1]); // Add a single row of values to the table
	}
}
