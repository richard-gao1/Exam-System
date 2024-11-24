package comp3111.examsystem.controller;

import comp3111.examsystem.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * This class is the controller for the student login page.
 * It provides methods for logging in and registering a new student account.
 */
public class StudentLoginController implements Initializable {
    @FXML
    private TextField usernameTxt;
    @FXML
    private PasswordField passwordTxt;

    /**
     * Initializes the controller.
     *
     * @param location the location of the FXML file
     * @param resources the resources used by the FXML file
     */
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Logs in the student.
     *
     * @param e the action event that triggered the login
     */
    @FXML
    public void login(ActionEvent e) {
        FXMLLoader studentMainLoader = new FXMLLoader();
        studentMainLoader.setLocation(Main.class.getResource("StudentMainUI.fxml"));
        Stage stage = new Stage();

        Student account = null;
        // try to login
        try{
            account = (Student)SystemDatabase.login(usernameTxt.getText(), passwordTxt.getText(), AccountType.STUDENT);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        if (account != null) {
            stage.setTitle("Hi " + account.getName() +", Welcome to HKUST Examination System");
            try {
                System.out.println("init student");
                SystemDatabase.currentUser = account;
                System.out.println("This is before loading");
                Parent root = studentMainLoader.load();
                StudentMainController mainController = studentMainLoader.getController();
                stage.setScene(new Scene(root));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            stage.show();
            ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Invalid username or password", ButtonType.OK);
            alert.setTitle("Login error");
            alert.show();
        }
    }

    /**
     * Registers a new student account.
     *
     * @param e the action event that triggered the registration
     */
    @FXML
    public void register(ActionEvent e) {
        FXMLLoader registerLoader = new FXMLLoader(Main.class.getResource("RegisterUI.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Register into the HKUST Examination System");
        try {
            stage.setScene(new Scene(registerLoader.load()));

            // Get the controller and pass the stage
            RegisterController controller = registerLoader.getController();

        } catch (IOException e1) {
            e1.printStackTrace();
        }

        stage.show();
        // No need to close the login menu
        //((Stage) ((Button) e.getSource()).getScene().getWindow()).close();

    }
}
