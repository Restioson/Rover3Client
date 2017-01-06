package io.github.restioson.rover3.client;

import java.io.ByteArrayInputStream;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainController {
	
	// Fields
	private Scene connectRoverScene;
	private ConnectRoverController connectController;
	private Main mainclass;
	
	@FXML
	private StackPane rootPane; // Pane which holds imageview and HUD on top level
	
	@FXML
	private Label speedDirectionAndTilt; // Speed, direction, and tilt label
	
	@FXML
	private Label dateAndTime; // Date and time label
	
	@FXML
	private Label tempAndHumidity; // Temp and humidity label
	
	@FXML
	private Label rangefinderReadings; // Rangefinder readings label
	
	@FXML
	private ImageView streamFrame; // Frame of stream
	
	
	@FXML
	public void connectRover() {
		Stage stage = new Stage();
		stage.setScene(connectRoverScene);
		stage.setAlwaysOnTop(true);
		stage.setTitle("Connect to Server");
		stage.setResizable(false);
		connectController.setStage(stage);
		stage.show();
		stage.requestFocus();
	}
	
	// Starts reading stream thread
	public void startListeningStream(String ip) {
		
		// Start thread
		new Thread(new Runnable() {

			// On run
			@Override
			public void run() {
				if (mainclass.connectToServer(ip)) {
					System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
					
					Mat frame = new Mat();
					VideoCapture capture = new VideoCapture(String.format("http://%s:8080/stream/video.h264", ip));
					MatOfByte buffer = new MatOfByte();
					
					
					while (true) {
						
						if (capture.isOpened()) {
							try {
								capture.read(frame);
								Imgcodecs.imencode(".png", frame, buffer);
								Image imageFrame = new Image(new ByteArrayInputStream(buffer.toArray()));
								
								Platform.runLater(new Runnable() {
									@Override public void run() {
										streamFrame.setImage(imageFrame);
									}
								});
							}
							
							catch (Exception e) {
								Platform.runLater(new Runnable() {
									@Override public void run() {
										Alert alert = new Alert(AlertType.ERROR);
										alert.setHeaderText("Connection Error");
										alert.setContentText(String.format("Error reading video stream: %s", e.getMessage()));
										alert.showAndWait();
									}
								});
								
							}
							
						}
					
					}
					
				}
			}
			
		}).start();
		
	}
	
	
	// Sets connect scene
	public void setConnectRoverScene(Scene connectScene) {
		connectRoverScene = connectScene;
	}
	
	// Sets connect controller
	public void setConnectController(ConnectRoverController controller) {
		connectController = controller;
	}

	public void setMainclass(Main main) {
		mainclass = main;
		streamFrame.setImage(new Image(getClass().getResourceAsStream("default.png")));
	}
	
}