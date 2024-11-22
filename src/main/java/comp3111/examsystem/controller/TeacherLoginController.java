package comp3111.examsystem.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import comp3111.examsystem.AccountType;
import comp3111.examsystem.Main;
import comp3111.examsystem.SystemDatabase;
import comp3111.examsystem.Teacher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class TeacherLoginController implements Initializable {
    @FXML
    private TextField usernameTxt;
    @FXML
    private PasswordField passwordTxt;
    @FXML
    private Button loginBtn;

    private Teacher teacher;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        passwordTxt.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) { // Check if Enter key is pressed
                loginBtn.fire(); // Programmatically click the button
            }
        });
    }

    @FXML
    public void login(ActionEvent e) {
        teacher = (Teacher) SystemDatabase.login(usernameTxt.getText(),passwordTxt.getText(), AccountType.TEACHER);

        if (teacher == null){
            Alert alert = new Alert(Alert.AlertType.NONE, "Invalid username or password", ButtonType.OK);
            alert.setTitle("Login error");
            alert.show();
            return;
        }
        SystemDatabase.currentUser = teacher;
        Alert alert = new Alert(Alert.AlertType.INFORMATION, null, ButtonType.OK);
        alert.setTitle("Hint");
        alert.setHeaderText("Login successful");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("TeacherMainUI.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Hi " + usernameTxt.getText() + ", Welcome to HKUST Examination System");
            try {
                stage.setScene(new Scene(fxmlLoader.load()));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            stage.show();
            ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
        }
    }

    @FXML
    public void register(ActionEvent e) {
        FXMLLoader registerLoader = new FXMLLoader(Main.class.getResource("RegisterUI.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Teacher Register");

        try {
            stage.setScene(new Scene(registerLoader.load()));

            // Get the controller and pass the stage
            RegisterController controller = registerLoader.getController();
            controller.teacherSet();

        } catch (IOException e1) {
            e1.printStackTrace();
        }

        stage.show();
        // no need to close.
        //((Stage) ((Button) e.getSource()).getScene().getWindow()).close();

    }
}
