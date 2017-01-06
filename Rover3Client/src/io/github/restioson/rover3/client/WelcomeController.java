package io.github.restioson.rover3.client;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

public class WelcomeController {
	
	// Fields
	private Stage welcomestage;
	private Scene mainscene;
	
	@FXML
	private CheckBox dontShowAgain;
	
	@FXML
	public void continueCallback() {
		Stage stage = new Stage();
		stage.setScene(mainscene);
		stage.setTitle("Rover3 Client");
		stage.setMinHeight(620d);
		stage.setMinWidth(970d);
		welcomestage.close();
		stage.show();
	}
	
	public void setMainScene(Scene mainScene) {
		mainscene = mainScene;
	}
	
	public void setStage(Stage welcomeStage) {
		welcomestage = welcomeStage;
	}
	
}
