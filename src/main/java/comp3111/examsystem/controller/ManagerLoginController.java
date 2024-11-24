package comp3111.examsystem.controller;

import comp3111.examsystem.AccountType;
import comp3111.examsystem.Main;
import comp3111.examsystem.Manager;
import comp3111.examsystem.SystemDatabase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller class for the Manager Login view.
 * This class is responsible for handling user interactions and managing the state of the Manager Login view.
 * It initializes the UI components, sets up the data bindings, and handles events such as logging in and navigating to the Manager Main view.
 */
public class ManagerLoginController implements Initializable {
    @FXML
    private TextField usernameTxt;
    @FXML
    private PasswordField passwordTxt;

    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Handles the manager login request from an actor.
     *
     * @param e The ActionEvent associated with the login button click.
     */
    @FXML
    public void login(ActionEvent e) {
        Manager manager = (Manager) SystemDatabase.login(usernameTxt.getText(), passwordTxt.getText(), AccountType.MANAGER);
        if (manager == null) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Invalid username or password", ButtonType.OK);
            alert.setTitle("Login error");
            alert.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, null, ButtonType.OK);
            alert.setTitle("Hint");
            alert.setHeaderText("Login successful");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ManagerMainUI.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Hi " + manager.getUsername() +", Welcome to HKUST Examination System");
                try {
                    stage.setScene(new Scene(fxmlLoader.load()));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                ManagerMainController mmc = fxmlLoader.getController();
                stage.show();
                ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
            }
        }
    }

}
