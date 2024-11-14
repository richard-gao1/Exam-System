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

public class ManagerLoginController implements Initializable {
    @FXML
    private TextField usernameTxt;
    @FXML
    private PasswordField passwordTxt;

    private Manager manager;

    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void login(ActionEvent e) {
        manager = (Manager) new SystemDatabase().login(usernameTxt.getText(), passwordTxt.getText(), AccountType.MANAGER);
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
                mmc.setManager(manager);
                stage.show();
                ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
            }
        }
    }

}
