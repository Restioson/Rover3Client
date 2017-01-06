package io.github.restioson.rover3.client;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ConnectRoverController {
	
	// Fields
	private Stage stage;
	private MainController controller;
	
	
	@FXML
	private TextField roverIP;
	
	@FXML
	public void connect () {
		
		// Get Rover IP
		String ip = roverIP.getText();
		controller.startListeningStream(ip);
		stage.close();
		
	}
	
	// Sets stage
	public void setStage(Stage connectStage) {
		stage = connectStage;
	}
	
	// Sets main controller
	public void setMainController(MainController mainController) {
		this.controller = mainController;
	}
	

}
