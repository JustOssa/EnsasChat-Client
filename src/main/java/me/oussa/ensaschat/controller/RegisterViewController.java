package me.oussa.ensaschat.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import me.oussa.ensaschat.ClientApplication;

import java.io.IOException;

public class RegisterViewController {

    @FXML
    private TextField usernameInput;

    private ClientController controller;

    @FXML
    public void initialize() {
        controller = ClientController.getInstance();
    }

    @FXML
    private void onGoSignin() {
        try {
            Stage stage = (Stage) usernameInput.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("Login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.setTitle("Sign in");

            ((LoginViewController) fxmlLoader.getController()).setStage(stage);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}
