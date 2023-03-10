package me.oussa.ensaschat.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import me.oussa.ensaschat.ClientApplication;
import me.oussa.ensaschat.model.User;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RegisterViewController {

    @FXML
    private TextField nameInput;
    @FXML
    private TextField usernameInput;
    @FXML
    private TextField passwordInput;

    private ClientController controller;

    @FXML
    public void initialize() {
        controller = ClientController.getInstance();
    }

    @FXML
    public void onSignUpClick() {
        User user = new User(nameInput.getText(), usernameInput.getText(), passwordInput.getText());
        try {
            controller.connectToServer();
            if (controller.signUp(user)) {
                onGoSignin();
                controller.showSuccess("Signup success", "You can now login");
            } else {
                controller.showError("Signup error", "Username already exists");
            }
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            controller.showError("Connection failed", "Could not connect to server");
        }
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
