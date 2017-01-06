package io.github.restioson.rover3.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
	private String ip;
	
	@Override
	public void start(Stage primaryStage) {
	    
		try {
			
			// FMXLoaders
			FXMLLoader rootLoader = new FXMLLoader(getClass().getResource("Main.fxml"));
			FXMLLoader connectLoader = new FXMLLoader(getClass().getResource("ConnectRover.fxml"));
			FXMLLoader welcomeLoader = new FXMLLoader(getClass().getResource("Welcome.fxml"));
			
			// Panes
			StackPane root = (StackPane) rootLoader.load();
			AnchorPane connectPane = (AnchorPane) connectLoader.load();
			BorderPane welcomePane = (BorderPane) welcomeLoader.load();
			
			// Controllers
			MainController controller = rootLoader.getController();
			ConnectRoverController connectController = connectLoader.getController();
			WelcomeController welcomeController = welcomeLoader.getController();
			
			// Scenes
			Scene scene = new Scene(root);
			Scene connectScene = new Scene(connectPane);
			Scene welcomeScene = new Scene(welcomePane);
			
			// Stylesheets
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			connectScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			welcomeScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			// Pass values to controllers
			controller.setConnectRoverScene(connectScene);
			controller.setConnectController(connectController);
			controller.setMainclass(this);
			welcomeController.setStage(primaryStage);
			welcomeController.setMainScene(scene);
			connectController.setStage(primaryStage);
			connectController.setMainController(controller);
			
			
			// Stage
			primaryStage.setScene(welcomeScene);
			primaryStage.setTitle("Rover3 Client");
			primaryStage.show();
			
		} 
		
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void stop() {
		// Connect to server and stop stream
		Socket socket;
		try {
			socket = new Socket(ip, 1895);
			ObjectOutputStream socketOut = new ObjectOutputStream(socket.getOutputStream());
			socketOut.writeObject("stop");
			socketOut.close();
			socket.close();	
		} 
		
		catch (IOException e) {}
		
		System.exit(0);

	}
	
	public boolean connectToServer(String serverIP) {
	    
		ip = serverIP;
		
		// Connect to server and start stream
		try {
			Socket socket = new Socket(ip, 1895);
			PrintWriter socketOut = new PrintWriter(socket.getOutputStream());
			socketOut.write("start");
			socketOut.close();
			socket.close();
			return true;
		} 
		
		catch (UnknownHostException e) {
			Platform.runLater(new Runnable() {
				@Override public void run() {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setHeaderText("Connection Error");
					alert.setContentText(String.format("Error connecting to the server: %s", e.getMessage()));
					alert.showAndWait();
				}
			});
			
			return false;
		} 
		
		catch (IOException e) {
			Platform.runLater(new Runnable() {
				@Override public void run() {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setHeaderText("Connection Error");
					alert.setContentText(String.format("Error connecting to the server: %s", e.getMessage()));
					alert.showAndWait();
				}
			});
			
			return false;
			
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
