import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class UserInput extends HBox{
	
	private Label label;
	TextField textField;
	
	public UserInput(String labelText) {
		this.label = new Label (labelText);
		this.textField = new TextField();
		this.getChildren().addAll(label, textField);
		
	}
	
	public String getText() {
		return textField.getText();
	}

}
