package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class HubController {
	
	@FXML
	private TextField txtMacAddress;
	
	@FXML
	private TextField txtIpAddress;
	
	@FXML
	private TextField txtAPIKey;
	
	@FXML
	private ListView deviceView;
	
	public void generateAPIKey() {
		
	}
}
