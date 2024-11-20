import javafx.scene.control.PasswordField;

public class PasswordInput extends UserInput {

    private PasswordField passwordField;

    // Constructor
    public PasswordInput(String labelText) {
        super(labelText);
        getChildren().remove(textField);
        this.passwordField = new PasswordField();
        this.getChildren().add(passwordField);
    }

    @Override
    public String getText() {
        return passwordField.getText();
    }
}