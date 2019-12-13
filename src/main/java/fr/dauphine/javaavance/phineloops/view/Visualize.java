package fr.dauphine.javaavance.phineloops.view;

import fr.dauphine.javaavance.phineloops.Main;
import javafx.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class Visualize extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("PhineLoops");
		BorderPane pane = new BorderPane();
		Scene scene = new Scene(pane);
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.show();
	}

	
}

