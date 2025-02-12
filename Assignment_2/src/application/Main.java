package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

//main class that runs the application
public class Main extends Application {
	private static Stage stg;
	@Override
	public void start(Stage primaryStage) {
		try {
			stg = primaryStage;
			primaryStage.setResizable(false);
			primaryStage.setTitle("LMVM");
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void changeScene(String fxml) throws IOException {
		BorderPane pane = (BorderPane)FXMLLoader.load(getClass().getResource(fxml));
		stg.getScene().setRoot(pane);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
