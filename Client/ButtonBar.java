import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ButtonBar extends HBox{
	
	public ButtonBar() {
		setAlignment(Pos.CENTER);
		setSpacing(10);
	}
	
	public void addButton(String text, EventHandler<ActionEvent> func) {
		Button button = new Button(text);
		button.setOnAction(func);
		getChildren().add(button);
	}

}
