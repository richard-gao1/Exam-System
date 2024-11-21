package comp3111.examsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The Main class extends the JavaFX Application class and serves as the entry point of the
 application.
 * It initializes the primary stage and loads the LoginUI.fxml file to display the login user
 interface.
 */
public class Main extends Application {
	/**
	 * The start method is called by the JavaFX runtime to initialize the primary stage.
	 *
	 * @param primaryStage The primary stage for this application, onto which
	 *                     the application scene can be set.
	 * @throws Exception if there is an error during initialization
	 */
	public void start(Stage primaryStage) {
		// load systemDatabase
		SystemDatabase database = new SystemDatabase();
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("LoginUI.fxml"));
			Scene scene = new Scene(fxmlLoader.load(), 640, 480);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * The main method is the entry point of the Java application.
	 * It launches the JavaFX application by calling the launch method from the Application
	 class.
	 *
	 * @param args Command line arguments passed to the application
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
