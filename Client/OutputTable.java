//Author: Christopher Robles
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class OutputTable extends GridPane{
	private int row = 1; //Next row for data
	
	public OutputTable(String... headers) {
		for (int col = 0; col < headers.length; col++) {
			Label headerLabel = new Label(headers[col]);
			this.add(headerLabel, col, 0);
		}
		
	}
	
	public void addRow(String... values) {
		for (int col = 0; col < values.length; col++) {
			Label valueLabel = new Label(values[col]);
			this.add(valueLabel, col, row);
		}
		row++;
	}
	
	public void clearData() {
		this.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) >= 1);
		row = 1;
	}

}
